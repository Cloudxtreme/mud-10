import moment from 'moment-timezone';
import uuid from 'node-uuid';

export class Token {
    constructor({startTime = moment(),
        expiryTime,
        tokenId = uuid.v4(),
        userId}) {

        if (typeof userId === 'undefined') {
            throw new Error('No User ID was provided');
        }
        if (typeof expiryTime === 'undefined') {
            throw new Error('No Expiry Time was provided');
        }
        if (!moment.isMoment(startTime)) {
            throw new Error('Start Time must be a moment object');
        }
        if (!moment.isMoment(expiryTime)) {
            throw new Error('Expiry Time must be a moment object');
        }
        if (!startTime.isBefore(expiryTime)) {
            throw new Error('Start time must be strictly before expiry time');
        }

        this._startTime = startTime;
        this._expiryTime = expiryTime;
        this._tokenId = tokenId;
        this._userId = userId;
    }

    get startTime() {
        return this._startTime;
    }

    get expiryTime() {
        return this._expiryTime;
    }

    get tokenId() {
        return this._tokenId;
    }

    get userId() {
        return this._userId;
    }
}
