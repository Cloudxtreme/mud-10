import {Token} from './token';
import moment from 'moment-timezone';

/** How long tokens are valid for. This is 1 day */
const VALIDITY = moment.duration('P1D');

/**
 * Issue an access token for the given user
 * @param {User} user The user to issue the access token for
 * @return {Token} the access token
 */
export function issueToken(user) {
    const now = moment();
    const expires = moment().add(VALIDITY);
    const token = new Token({
        startTime: now,
        expiryTime: expires,
        userId: user.id
    });
    return token;
}
