import {expect} from 'chai';
import moment from 'moment-timezone';
import sinon from 'sinon';
import {Token} from '../token';
import {encodeToken, decodeToken, __RewireAPI__ as TokenEncoderRewire} from '../tokenEncoder';

describe('TokenEncoder', function() {
    describe('encodeToken', function() {
        const start = moment().subtract(30, 'minutes');
        const tokenId = 'thisIsTheTokenId';
        const userId = 'thisIsTheUserId';
        const signedToken = 'thisIsTheSignedToken';

        describe('invalid token', function() {
            it('fails when provided with the wrong type', function() {
                expect(() => encodeToken('hello')).to.throw();
            });
            it('fails when expiry time is in the past', function() {
                const end = moment().subtract(1, 'minutes');

                const token = new Token({startTime: start,
                    expiryTime: end,
                    tokenId,
                    userId});

                    expect(() => encodeToken(token)).to.throw();
            });
        });

        describe('valid token', function() {
            const end = moment().add(1, 'hours');

            const token = new Token({startTime: start,
                expiryTime: end,
                tokenId,
                userId});

            it('should attempt to generate a valid JWT', function() {
                const stubJwt = sinon.stub();
                stubJwt.callsArgWith(3, signedToken);

                TokenEncoderRewire.__Rewire__('jwt', {
                    sign: stubJwt
                });
                return encodeToken(token).then(function(jwt) {
                    expect(stubJwt.callCount).to.equal(1);
                    expect(stubJwt.firstCall.args[0]).to.deep.equal({});
                    expect(stubJwt.firstCall.args[1]).to.equal('abcdef');
                    expect(stubJwt.firstCall.args[2]).to.have.all.keys(
                        'algorithm',
                        'audience',
                        'issuer',
                        'expiresIn',
                        'notBefore',
                        'jwtid',
                        'subject'
                    );
                    expect(stubJwt.firstCall.args[2]).to.have.property('algorithm', 'HS256');
                    expect(stubJwt.firstCall.args[2]).to.have.property('audience', 'uk.co.grahamcox.mud.jwt');
                    expect(stubJwt.firstCall.args[2]).to.have.property('issuer', 'uk.co.grahamcox.mud.jwt');
                    expect(stubJwt.firstCall.args[2]).to.have.property('jwtid', tokenId);
                    expect(stubJwt.firstCall.args[2]).to.have.property('subject', userId);
                    expect(stubJwt.firstCall.args[2]).to.have.property('expiresIn').within(3598, 3602);
                    expect(stubJwt.firstCall.args[2]).to.have.property('notBefore').within(-1802, -1798);

                    expect(jwt).to.equal(signedToken);
                });
            });

            it('works when doing it for real', function() {
                return encodeToken(token).then(function(jwt) {
                    console.log(jwt);
                    expect(jwt).to.be.a.string;
                    expect(jwt).to.match(/^[A-Za-z0-9-_=]+\.[A-Za-z0-9-_=]+\.[A-Za-z0-9-_.+/=]+$/);
                });
            });

            afterEach(function() {
                TokenEncoderRewire.__ResetDependency__('jwt');
            });
        });
    });

    describe('decodeToken', function() {
        describe('An invalid token', function() {
            it('does not decode cleanly', function() {
                return expect(decodeToken('abcdef')).to.eventually.be.rejected;
            });
        });
        describe('A valid token', function() {
            const start = moment().subtract(30, 'minutes');
            const end = moment().add(1, 'hours');
            const tokenId = 'thisIsTheTokenId';
            const userId = 'thisIsTheUserId';

            let encodedToken;

            before(function() {
                const token = new Token({startTime: start,
                    expiryTime: end,
                    tokenId,
                    userId});
                return encodeToken(token).then(function(jwt) {
                    encodedToken = jwt;
                });
            });

            it('decodes cleanly', function() {
                return decodeToken(encodedToken);
            });
            it('has the correct Token ID', function() {
                return decodeToken(encodedToken)
                    .then(token => {
                        expect(token.tokenId).to.equal(tokenId);
                    });
            });
            it('has the correct User ID', function() {
                return decodeToken(encodedToken)
                    .then(token => {
                        expect(token.userId).to.equal(userId);
                    });
            });
            it('has the correct start time', function() {
                return decodeToken(encodedToken)
                    .then(token => {
                        expect(token.startTime.diff(start, 'seconds')).to.be.within(-1, 1);
                    });
            });
            it('has the correct expiry time', function() {
                return decodeToken(encodedToken)
                    .then(token => {
                        expect(token.expiryTime.diff(end, 'seconds')).to.be.within(-1, 1);
                    });
            });
        });
    });
});
