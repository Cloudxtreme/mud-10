import Joi from 'joi';

function handleTokenRoute(params) {
    console.log(params);
    return {
        'access_token': 'abcdef',
        'token_type': 'Bearer',
        'expires_in': 3600,
        'refresh_token': '123456'
    };
}

function buildRoute(method, paramSource) {
    return {
        method,
        path: '/api/oauth2/token',
        config: {
            tags: ['api', 'oauth2'],
            description: 'OAuth2 Token endpoint',
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
                reply(handleTokenRoute(request[paramSource]));
            }
        }
    };
}

export const routes = [buildRoute('GET', 'query'), buildRoute('POST', 'payload')];
