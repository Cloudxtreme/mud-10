import Boom from 'boom';
import {decodeToken} from '../../authentication/tokenEncoder';

const AUTH_SCHEME = 'Bearer';
const BEARER_PREFIX = AUTH_SCHEME + ' ';

/**
 * The actual implementation of the OAuth2 Scheme that we are going to use
 * @param {Server} server The server to work with
 * @param {Object} options The options to the scheme
 * @return {Object} the implementation of the scheme to use
 */
function oauth2Scheme(server, options) {
    return {
        authenticate: (request, reply) => {
            const authHeader = request.headers.authorization;

            try {
                if (typeof authHeader === 'undefined') {
                    throw 'No token provided';
                }
                if (!authHeader.startsWith(BEARER_PREFIX)) {
                    throw 'Unsupported authentication type provided';
                }
            } catch (e) {
                return reply(Boom.unauthorized(e, AUTH_SCHEME));
            }

            const token = authHeader.substr(BEARER_PREFIX.length);
            return decodeToken(token)
                .then(accessToken => {
                    return reply.continue({
                        credentials: {
                            rawToken: token,
                            accessToken: accessToken
                        }
                    });
                })
                .catch(err => {
                    console.log(err);
                    return reply(Boom.unauthorized('Invalid access token', AUTH_SCHEME), null, {
                        credentials: {
                            rawToken: token
                        }
                    });
                });
        }
    }
}

/**
 * Register the plugin with a server
 * @param {Server} server The server to register with
 * @param {Object} options The plugin options to register with
 * @param {Function} next The next function when we're finished registering
 */
export function register(server, options, next) {
    console.log('Registering authentication plugin');
    server.auth.scheme('oauth2', oauth2Scheme);
    server.auth.strategy('oauth2', 'oauth2', false, {
        validToken: 'abcdef'
    });
    next();
}

register.attributes = {
    name: 'AuthenticationPlugin',
    version: '1.0.0'
};
