import Joi from 'joi';
import status from 'hapi-status';

export function routes(server) {
    server.route([{
        method: 'GET',
        path: '/api/debug/who',
        config: {
            auth: 'oauth2',
            tags: ['api', 'debug'],
            description: 'Debug handler to get details of the current user',
            response: {
                schema: Joi.object({
                    token: Joi.string().description('The token that the user authenticated with').example('abcdef').required()
                }).required().options({
                    allowUnknown: true,
                    stripUnknown: false
                })
            },
            handler: function(request, reply) {
                return reply({
                    token: request.auth.credentials.token
                });
            }
        }
    }, {
        method: 'GET',
        path: '/api/debug/ping',
        config: {
            id: 'ping',
            tags: ['api', 'debug'],
            description: 'Debug handler to get a response back from the server',
            response: {
                schema: Joi.object({
                    ping: Joi.string().description('The value sent in the ping, or "Pong" otherwise').example('Pong').required()
                }).description('The Ping response').required().options({
                    allowUnknown: true,
                    stripUnknown: false
                })
            },
            handler: {
                versioned: {
                    'v1.0': function(request, reply) {
                        const result = {
                            ping: 'Pong',
                            url: request.to('ping')
                        };
                        return reply(result);
                    },
                    'v2.0': function(request, reply) {
                        const result = {
                            ping: 'Pong'
                        };
                        return reply(result);
                    }
                }
            }
        }
    }]);
}
