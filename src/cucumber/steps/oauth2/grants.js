import request from 'request';
import {expect} from 'chai';

module.exports = function () {
    this.When(/^I request a username password credentials grant for:$/, function (userDetails, callback) {
        const userDetailsHash = userDetails.rowsHash();
        if (!('Username' in userDetailsHash)) {
            throw new Error('No username provided');
        }
        if (!('Password' in userDetailsHash)) {
            throw new Error('No password provided');
        }

        this.request.post('/api/oauth2/token', {
            grant_type: 'password',
            username: userDetailsHash.Username,
            password: userDetailsHash.Password
        }).then(() => callback(), (error) => callback(error));
    });

    this.Then(/^I get a successful access token response$/, function() {
        expect(this.request.lastResponse()).to.be.defined;
        expect(this.request.lastResponse().statusCode).to.equal(200);
        expect(this.request.lastResponse().body).to.be.an.object;
        expect(this.request.lastResponse().body).to.have.property('access_token');
        expect(this.request.lastResponse().body).to.have.property('token_type', 'Bearer');
        expect(this.request.lastResponse().body).to.have.property('expires_in').within(86395, 86405);
    });

    this.Then(/^I get an error response of "([^"]+)"$/, function(errorCode) {
        expect(this.request.lastResponse()).to.be.defined;
        expect(this.request.lastResponse().statusCode).to.equal(400);
        expect(this.request.lastResponse().body).to.be.an.object;
        expect(this.request.lastResponse().body).to.have.property('error', errorCode);
    });
};
