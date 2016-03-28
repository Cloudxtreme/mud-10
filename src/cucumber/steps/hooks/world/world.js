import {request} from './request';

module.exports = function() {
    this.World = function World() {
        this.request = request;
    }
};
