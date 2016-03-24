/**
 * Register the plugin with a server
 * @param {Server} server The server to register with
 * @param {Object} options The plugin options to register with
 * @param {Function} next The next function when we're finished registering
 */
export function register(server, options, next) {
    console.log('Registering authentication plugin');
    next();
}

register.attributes = {
    name: 'AuthenticationPlugin',
    version: '1.0.0'
};
