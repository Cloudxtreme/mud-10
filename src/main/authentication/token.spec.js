import {expect} from 'chai';
import moment from 'moment-timezone';
import {Token} from './token';

describe('Token', function() {
    describe('Full constructor', function() {
        const start = moment();
        const end = moment().add(1, 'hours');
        const tokenId = 'thisIsTheTokenId';
        const userId = 'thisIsTheUserId';

        const token = new Token({startTime: start,
            expiryTime: end,
            tokenId,
            userId});
        it('should return the correct start time', function() {
            expect(token.startTime).to.equal(start);
        });
        it('should return the correct expiry time', function() {
            expect(token.expiryTime).to.equal(end);
        });
        it('should return the correct Token ID', function() {
            expect(token.tokenId).to.equal(tokenId);
        });
        it('should return the correct User ID', function() {
            expect(token.userId).to.equal(userId);
        });
    });
    describe('Minimal constructor', function() {
        const end = moment().add(1, 'hours');
        const userId = 'thisIsTheUserId';

        const token = new Token({expiryTime: end,
            userId});
        it('should return a start time', function() {
            expect(token.startTime).to.be.defined;
        });
        it('should return the correct expiry time', function() {
            expect(token.expiryTime).to.equal(end);
        });
        it('should return a Token ID', function() {
            expect(token.tokenId).to.be.defined;
        });
        it('should return the correct User ID', function() {
            expect(token.userId).to.equal(userId);
        });
    });
    describe('Invalid construction', function() {
        it('should throw when no expiry time provided', function() {
            expect(() => {
                new Token({userId: 'userId'});
            }).to.throw(Error);
        });
        it('should throw when no User ID provided', function() {
            expect(() => {
                new Token({expiryTime: moment()});
            }).to.throw(Error);
        });
        it('should throw when expiry time is not a moment', function() {
            expect(() => {
                new Token({
                    userId: 'userId',
                    expiryTime: 'now'
                });
            }).to.throw(Error);
        });
        it('should throw when start time is not a moment', function() {
            expect(() => {
                new Token({
                    userId: 'userId',
                    expiryTime: moment(),
                    startTime: 'now'
                });
            }).to.throw(Error);
        });
        it('should throw when expiry time is equal to start time', function() {
            const start = moment();
            expect(() => {
                new Token({
                    startTime: start,
                    expiryTime: start,
                    userId: 'userId'
                })
            }).to.throw(Error);
        });
        it('should throw when expiry time is before start time', function() {
            const start = moment();
            const end = moment().subtract(1, 'hours');
            expect(() => {
                new Token({
                    startTime: start,
                    expiryTime: end,
                    userId: 'userId'
                })
            }).to.throw(Error);
        });
    });
});
