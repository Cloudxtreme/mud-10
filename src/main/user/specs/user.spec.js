import {expect} from 'chai';
import {User} from '../user';
import {Password} from '../password';

describe('User', function() {
    describe('Constructor', function() {
        const password = new Password('hash', 'salt');
        const user = new User('abcdef', 'graham@grahamcox.co.uk', password, true);

        it('returns the correct ID', function() {
            expect(user.id).to.equal('abcdef');
        });
        it('returns the correct Email Address', function() {
            expect(user.email).to.equal('graham@grahamcox.co.uk');
        });
        it('returns the correct Password', function() {
            expect(user.password).to.equal(password);
        });
        it('returns the correct Enabled flag', function() {
            expect(user.enabled).to.be.true;
        });
    });
});
