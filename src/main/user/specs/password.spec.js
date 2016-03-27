import {expect} from 'chai';
import sinon from 'sinon';
import {Password, hashPassword, __RewireAPI__ as PasswordRewire} from '../password';

describe('Password', function() {
    describe('Constructor', function() {
        const password = new Password('thisIsTheHash', 'thisIstheSalt');
        it('returns the correct hash', function() {
            expect(password.hash).to.equal('thisIsTheHash');
        });
        it('returns the correct salt', function() {
            expect(password.salt).to.equal('thisIstheSalt');
        });
    });
    describe('hashPassword', function() {
        describe('With provided salt', function() {
            const password = hashPassword('password', 'salt');
            it('returns the correct hash', function() {
                // SHA-256 HMAC of 'password' with a secret of 'salt'
                expect(password.hash).to.equal('84ec44c7d6fc41917953a1dafca3c7d7856f7a9d0328b991b76f0d36be1224b9');
            });
            it('returns the correct salt', function() {
                expect(password.salt).to.equal('salt');
            });
        });
        describe('With Generated salt', function() {
            const salt = 'pepper';
            const stubUuid = sinon.stub();
            stubUuid.onFirstCall().returns(salt);
            PasswordRewire.__Rewire__('v4', stubUuid);

            const password = hashPassword('password');
            it('returns the correct hash', function() {
                // SHA-256 HMAC of 'password' with a secret of 'pepper'
                expect(password.hash).to.equal('99539b96f7212c979b5a79755d6bade0c3cdc6420b96543c1a949f9ab859bea1');
            });
            it('returns the correct salt', function() {
                expect(password.salt).to.equal(salt);
            });
            it('Called the UUID as expected', function() {
                expect(stubUuid.callCount).to.equal(1);
            });

            afterEach(function() {
                PasswordRewire.__ResetDependency__('v4');
            });
        });
    });
    describe('equals', function() {
        const password = hashPassword('password', 'salt');
        describe('plaintext', function() {
            it('works with the correct password', function() {
                expect(password.equals('password')).to.be.true;
            })
            it('fails with the wrong password', function() {
                expect(password.equals('somethingElse')).to.be.false;
            })
            it('fails with the correct in the wrong case', function() {
                expect(password.equals('Password')).to.be.false;
            })
        });
        describe('hashed', function() {
            it('works with the correct password', function() {
                expect(password.equals(new Password(password.hash, password.salt))).to.be.true;
            });
            it('fails with the wrong password', function() {
                expect(password.equals(new Password('wrongpassword', password.salt))).to.be.false;
            });
            it('fails with the wrong salt', function() {
                expect(password.equals(new Password(password.hash, 'wrongsalt'))).to.be.false;
            });
        });
        describe('Something else', function() {
            it('fails with a number', function() {
                expect(() => {password.equals(1)}).to.throw;
            });
            it('fails with an object', function() {
                expect(() => {password.equals({})}).to.throw;
            });
            it('fails with an array', function() {
                expect(() => {password.equals([])}).to.throw;
            });
        });
    });
});
