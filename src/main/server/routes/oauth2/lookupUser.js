import {findUserByEmail} from '../../../user/userLoader';

/**
 * Look up the user details for the given email address and password
 * @param {String} email The email address to look up
 * @param {String} password The password to match against
 * @return {Promise} a Promise for the user details. If rejected then the rejection is an OAuth2 error payload
 */
export function lookupUser(email, password) {
    return findUserByEmail(email)
        .then((user) => {
            console.log('Checking password');
            if (!user.password.equals(password)) {
                throw new Error('Invalid password');
            }
            return user;
        })
        .catch((err) => {
            console.log('Error getting user details: ' + err);
            throw {
                error: 'invalid_grant',
                error_description: 'Invalid Username or Password'
            };
        })
        .then((user) => {
            console.log('Checking if user is enabled');
            if (!user.enabled) {
                throw {
                    error: 'invalid_grant',
                    error_description: 'User account is banned'
                };
            }
            return user;
        })
}
