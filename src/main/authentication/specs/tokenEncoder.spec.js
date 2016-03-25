import {expect} from 'chai';
import moment from 'moment-timezone';
import {Token} from '../token';
import {encodeToken} from '../tokenEncoder';

describe('TokenEncoder', function() {
    describe('encodeToken', function() {
        describe('valid token', function() {
            const start = moment().add(30, 'minutes');
            const end = moment().add(1, 'hours');
            const tokenId = 'thisIsTheTokenId';
            const userId = 'thisIsTheUserId';

            const token = new Token({startTime: start,
                expiryTime: end,
                tokenId,
                userId});

            it('should return a valid token', function() {
                return encodeToken(token).then(function(jwt) {
                    console.log(jwt);
                });
            });
        });
    });
});
