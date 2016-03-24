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

            if (authHeader) {
                reply.continue({
                    credentials: {
                        token: authHeader
                    }
                });
            } else {
                reply({error: 'No token present'});
            }
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
