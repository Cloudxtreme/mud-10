import Joi from 'joi';

export function routes(server) {
    server.route([{
        method: 'GET',
        path: '/api/debug/ping',
        config: {
            tags: ['api', 'debug'],
            description: 'Debug handler to get a response back from the server',
            response: {
                schema: Joi.object({
                    ping: Joi.string().description('The value sent in the ping, or "Pong" otherwise').example('Pong').required()
                }).description('The Ping response').required().options({
                    allowUnknown: true,
                    stripUnknown: false
                })
            }
        },
        handler: function(request, reply) {
            return reply({ping: 'Pong'});
        }
    }]);
}
