import {encodeToken} from '../../../authentication/tokenEncoder';
import moment from 'moment-timezone';

/**
 * Render the provided Access Token in the correct format for the OAuth2 response
 * @param {Token} token The access token to renderToken
 * @return {Promise} a promise for the rendered token
 */
export function renderToken(token) {
    return encodeToken(token)
        .then((encodedToken) => {
            const now = moment();
            const expiryPeriod = token.expiryTime.diff(now, 'seconds');

            return {
                access_token: encodedToken,
                token_type: 'Bearer',
                expires_in: expiryPeriod
            };
        });
}
