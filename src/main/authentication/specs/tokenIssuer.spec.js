import {expect} from 'chai';
import moment from 'moment-timezone';
import {issueToken} from '../tokenIssuer';
import {User} from '../../user/user';
import {Password} from '../../user/password';

describe('TokenIsuer', function() {
    describe('issueToken', function() {
        const user = new User('abcdef',
            'graham@grahamcox.co.uk',
            new Password('hash', 'salt'),
            true);
        const token = issueToken(user);

        it('is for the correct user', function() {
            expect(token.userId).to.equal(user.id);
        });
        it('has the correct validity period', function() {
            const validity = token.expiryTime.diff(token.startTime);
            expect(validity).to.be.within(86399998, 86400002); // 1 day in millis
        });
    });
});
