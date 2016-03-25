import config from 'config';
import Glue from 'glue';

const manifest = {
    server: {

    },
    connections: [
        {
            port: config.get('server.web.port'),
            labels: ['web']
        }
    ],
    registrations: [{
        plugin: 'inert'
    }, {
        plugin: 'vision'
    }, {
        plugin: 'blipp'
    }, {
        plugin: {
            register: 'consistency',
            options: {
                uriParam: 'apiVersion',
                acceptNamespace: 'mud',
                customHeaderKey: 'api-version'
            }
        }
    }, {
        plugin: {
            register: 'good',
            options: {
                opsInterval: 30000,
                responsePayload: true,
                reporters: [{
                    reporter: 'good-console',
                    events: {
                        ops: '*',
                        request: '*',
                        response: '*',
                        log: '*',
                        error: '*'
                    }
                }]
            }
        }
    }, {
        plugin: './auth/authPlugin'
    }, {
        plugin: {
            register: 'hale',
            options: {
                path: '/api/health',
                tags: ['health', 'monitor', 'api'],
                metadata: {
                    name: 'mud'
                }
            }
        }
    }, {
        plugin: {
            register: 'hapi-info',
            options: {
                path: '/api/info'
            }
        }
    }, {
        plugin: {
            register: './routes/routesPlugin',
            options: {
                files: `${__dirname}/routes/**/*.route.js`
            }
        }
    }, {
        plugin: 'hapi-routes-status',
        options: {
            routes: {
                prefix: '/api'
            }
        }
    }, {
        plugin: 'hapi-status-cat'
    }, {
        plugin: {
            register: 'hapi-swaggered',
            options: {
                endpoint: '/api/swagger',
                stripPrefix: '/api',
                info: {
                    title: 'WebMUD',
                    description: 'Web based MUD',
                    version: '1.0'
                },
                tagging: {
                    mode: 'path',

                },
                routeTags: ['api']
            }
        }
    }, {
        plugin: {
            register: 'hapi-swaggered-ui',
            options: {
                path: '/api/docs'
            }
        }
    }, {
        plugin: 'hapi-to'
    }]
}

export function startServer() {
    return new Promise((resolve, reject) => {
        const options = {
            relativeTo: __dirname
        };

        Glue.compose(manifest, options, (err, server) => {
            if (err) {
                reject(err);
            } else {
                server.start(() => {
                    resolve(server);
                });
            }
        })
    });
}
