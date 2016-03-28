module.exports = function () {
    this.Given(/^a user exists with details:$/, function (userDetails, callback) {
        console.log(userDetails.rowsHash());
        callback();
    });
};
