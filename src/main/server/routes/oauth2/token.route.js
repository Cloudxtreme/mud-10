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
            handler: function(request, reply) {
                reply(handleTokenRoute(request[paramSource]));
            }
        }
    };
}

export const routes = [buildRoute('GET', 'query'), buildRoute('POST', 'payload')];
