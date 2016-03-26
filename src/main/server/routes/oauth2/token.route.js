import Joi from 'joi';
import Boom from 'boom';

/**
 * Handle a Resource Owner Password Credentials grant
 * @param {String} username The username to authenticate for
 * @param {String} password The password to authenticate for
 * @param {String} scope The scopes to authenticate for
 * @param {Reply} reply The reply interface
 */
function handlePasswordToken({username, password, scope}, reply) {
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
        reply({
            access_token: 'abcdef',
            token_type: 'Bearer',
            expires_in: 3600
        });
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
