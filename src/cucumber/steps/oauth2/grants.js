import request from 'request';

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
        }, function(error, response, body) {
            console.log(response.statusCode);
            console.log(body);
            callback();
        });
    });
};
