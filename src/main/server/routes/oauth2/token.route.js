import Joi from 'joi';
import Boom from 'boom';
import {findUserByEmail} from '../../../user/userLoader';
import {issueToken} from '../../../authentication/tokenIssuer';
import {encodeToken} from '../../../authentication/tokenEncoder';
import moment from 'moment-timezone';

/**
 * Look up the user details for the given email address and password
 * @param {String} email The email address to look up
 * @param {String} password The password to match against
 * @return {Promise} a Promise for the user details. If rejected then the rejection is an OAuth2 error payload
 */
function lookupUser(email, password) {
    return findUserByEmail(email)
        .then((user) => {
            console.log('Checking password');
            if (!user.password.equals(password)) {
                throw new Error('Invalid password');
            }
            return user;
        })
        .catch((err) => {
            console.log('Error getting user details: ' + err);
            throw {
                error: 'invalid_grant',
                error_description: 'Invalid Username or Password'
            };
        })
        .then((user) => {
            console.log('Checking if user is enabled');
            if (!user.enabled) {
                throw {
                    error: 'invalid_grant',
                    error_description: 'User account is banned'
                };
            }
            return user;
        })
}

/**
 * Render the provided Access Token in the correct format for the OAuth2 response
 * @param {Token} token The access token to renderToken
 * @return {Promise} a promise for the rendered token
 */
function renderToken(token) {
    return encodeToken(token)
        .then((encodedToken) => {
            const now = moment();
            const expiryPeriod = token.expiryTime.diff(now, 'seconds');

            return {
                access_token: encodedToken,
                token_type: 'Bearer',
                expires_in: expiryPeriod
            };
        });
}

/**
 * Handle a Resource Owner Password Credentials grant
 * @param {String} username The username to authenticate for
 * @param {String} password The password to authenticate for
 * @param {String} scope The scopes to authenticate for
 * @param {Reply} reply The reply interface
 */
async function handlePasswordToken({username, password, scope}, reply) {
    const missingParams = [];
    if (typeof username === 'undefined') {
        missingParams.push('username');
    }
    if (typeof password === 'undefined') {
        missingParams.push('password');
    }
    if (missingParams.length > 0) {
        const response = reply({
            error: 'invalid_request',
            error_description: `Missing required parameters: ${missingParams}`
        });
        response.statusCode = 400;
    } else {
        try {
            const user = await lookupUser(username, password);
            console.log(`Got user: ${user}`);
            const accessToken = issueToken(user);
            const renderedToken = await renderToken(accessToken);
            reply(renderedToken);
        } catch (e) {
            console.log(e);
            const response = reply(e);
            response.statusCode = 400;
        }
    }
}

/**
 * Perform the actual controller functionality for the token route.
 * For now this is trivial because the route definition only allows for a Grant Type of password.
 * As future OAuth2 functionality is implemented this will be more complicated
 * @param {Object} params The received parameters
 * @param {Reply} reply The reply interface
 */
function handleTokenRoute(params, reply) {
    handlePasswordToken(params, reply);
}

/**
 * Build the route definition to use for the Token endpoint.
 * This exists so that we can map the exact same route definition onto GET and POST, but covering
 * the fact that GET uses querystring and POST uses payload
 * @param {String} method The method to build for
 * @param {String} paramSource the source of parameters
 * @return {Object} the route definition
 */
function buildRoute(method, paramSource) {
    const route = {
        method,
        path: '/api/oauth2/token',
        config: {
            tags: ['api', 'oauth2'],
            description: 'OAuth2 Token endpoint',
            validate: {},
            response: {
                status: {
                    200: Joi.object({
                        'access_token': Joi.string().required().description('The actual Access Token to use'),
                        'token_type': Joi.string().required().example('Bearar').description('The type of Access Token provided'),
                        'expires_in': Joi.number().required().example(3600).description('The number of seconds that the Access Token is valid for'),
                        'refresh_token': Joi.string().description('A Refresh Token to use to re-issue this Access Token')
                    }).required().description('Details of the issued Access Token'),
                    400: Joi.object({
                        'error': Joi.string().required().description('The error code'),
                        'error_description': Joi.string().description('The actual error reason')
                    }).required().description('Details of an error issuing the Access Token')
                }
            },
            handler: function(request, reply) {
                handleTokenRoute(request[paramSource], reply);
            }
        }
    };
    route.config.validate[paramSource] = Joi.object({
        'grant_type': Joi.string().required().description('The grant type to perform').only('password'),
        'scope': Joi.string().description('The OAuth2 Scopes to request for the token'),
        'username': Joi.string().description('If the grant_type is "password" then this is the username to authenticate as'),
        'password': Joi.string().description('If the grant_type is "password" then this is the password to authenticate with')
    });
    return route;
}

export const routes = [buildRoute('GET', 'query'), buildRoute('POST', 'payload')];
