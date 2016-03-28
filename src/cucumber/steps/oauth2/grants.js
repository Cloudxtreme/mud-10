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

        request({
            method: 'POST',
            uri: `${this._serverBase}/api/oauth2/token`,
            form: {
                grant_type: 'password',
                username: userDetailsHash.Username,
                password: userDetailsHash.Password
            }
        }, (error, response, body) => {
            this._response = {
                body: JSON.parse(body),
                headers: response.headers,
                statusCode: response.statusCode
            };
            callback();
        });
    });

    this.Then(/^I get a successful access token response$/, function() {
        expect(this._response).to.be.defined;
        expect(this._response.statusCode).to.equal(200);
        expect(this._response.body).to.be.an.object;
        expect(this._response.body).to.have.property('access_token');
        expect(this._response.body).to.have.property('token_type', 'Bearer');
        expect(this._response.body).to.have.property('expires_in').within(86395, 86405);
    });

    this.Then(/^I get an error response of "([^"]+)"$/, function(errorCode) {
        expect(this._response).to.be.defined;
        expect(this._response.statusCode).to.equal(400);
        expect(this._response.body).to.be.an.object;
        expect(this._response.body).to.have.property('error', errorCode);
    });
};
