import jwt from 'jsonwebtoken';
import config from 'config';
import moment from 'moment-timezone';
import {Token} from './token';

const ISSUER = 'uk.co.grahamcox.mud.jwt';

/**
 * Encode the given Authentication Token into a simple string
 * @param {Token} token The token to encode
 * @return {Promise} a promise for the encoded token
 */
export function encodeToken(token) {
    if (!token instanceof Token) {
        throw new Error('Provided token is not of correct type');
    }

    const now = moment();
    const untilExpires = token.expiryTime.diff(now, 'seconds');
    const untilStart = token.startTime.diff(now, 'seconds');

    if (!now.isBefore(token.expiryTime)) {
        throw new Error('Expiry time must be in the future');
    }

    return new Promise(function(resolve, reject) {

        jwt.sign({

        }, config.get('authentication.signingKey'), {
            algorithm: 'HS256',
            audience: ISSUER,
            expiresIn: untilExpires,
            notBefore: untilStart,
            subject: token.userId,
            issuer: ISSUER,
            jwtid: token.tokenId
        }, function(jwt) {
            resolve(jwt);
        });
    });
}
