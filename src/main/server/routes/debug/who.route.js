import Joi from 'joi';

export const routes = {
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
                token: request.auth.credentials.rawToken,
                accessToken: request.auth.credentials.accessToken
            });
        }
    }
};
