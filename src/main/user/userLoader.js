import {User} from './user';
import {hashPassword} from './password';

/**
 * Try and find a user with the given Email address
 * @param {String} email The email address to look for
 * @return {Promise} a promise for the user record
 */
export function findUserByEmail(email) {
    return new Promise((resolve, reject) => {
        if (email === 'graham@grahamcox.co.uk') {
            resolve(new User('abcdef', email, hashPassword('password'), true));
        } else if (email === 'banned@grahamcox.co.uk') {
            resolve(new User('123456', email, hashPassword('password'), false));
        } else {
            reject();
        }
    });
}
