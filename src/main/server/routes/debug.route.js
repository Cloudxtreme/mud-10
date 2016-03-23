export function routes(server) {
    server.route([{
        method: 'GET',
        path: '/api/debug/ping',
        config: {
            handler: function(request, reply) {
                return reply({ping: 'Pong'});
            }
        }
    }]);
}
