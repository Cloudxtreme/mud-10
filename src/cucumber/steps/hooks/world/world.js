import {request} from './request';

module.exports = function() {
    this.World = function World() {
        this.request = request;

        this.reset = function() {
            this.request.reset();
        };
    }

    this.Before(function(scenario) {
        this.reset();
    });
};
