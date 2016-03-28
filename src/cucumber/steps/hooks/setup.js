import 'babel-polyfill';

import {startServer} from '../../../mud/server';

let _server;

module.exports = function() {
    this.registerHandler('BeforeFeatures', function(event, callback) {
        startServer({
            port: 0
        }).then((server) => {
            console.log(`Started server on ${server.info.uri}`);
            _server = server;
            callback();
        });
    });

    this.Before(function(scenario) {
        if (_server) {
            this.request.setUrlBase(_server.info.uri);
        }
    });

    this.registerHandler('AfterFeatures', function(event, callback) {
        console.log(`Stopping server on ${_server.info.uri}`);
        if (_server) {
            _server.stop({
                timeout: 100
            }).then((server) => {
                console.log(`Server on ${_server.info.uri} stopped`);
                callback();
            });
        }
    });
}
