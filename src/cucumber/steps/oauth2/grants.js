module.exports = function () {
    this.When(/^I request a username password credentials grant for:$/, function (userDetails, callback) {
        console.log(userDetails.rowsHash());
        console.log(this._serverBase);
        callback();
    });
};
