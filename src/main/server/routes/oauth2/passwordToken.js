import {lookupUser} from './lookupUser';
import {renderToken} from './renderToken';
import {issueToken} from '../../../authentication/tokenIssuer';

/**
 * Handle a Resource Owner Password Credentials grant
 * @param {String} username The username to authenticate for
 * @param {String} password The password to authenticate for
 * @param {String} scope The scopes to authenticate for
 * @param {Reply} reply The reply interface
 */
export async function handlePasswordToken({username, password, scope}, reply) {
    const missingParams = [];
    if (typeof username === 'undefined') {
        missingParams.push('username');
    }
    if (typeof password === 'undefined') {
        missingParams.push('password');
    }
    if (missingParams.length > 0) {
        const response = reply({
            error: 'invalid_request',
            error_description: `Missing required parameters: ${missingParams}`
        });
        response.statusCode = 400;
    } else {
        try {
            const user = await lookupUser(username, password);
            console.log(`Got user: ${user}`);
            const accessToken = issueToken(user);
            const renderedToken = await renderToken(accessToken);
            reply(renderedToken);
        } catch (e) {
            console.log(e);
            const response = reply(e);
            response.statusCode = 400;
        }
    }
}
