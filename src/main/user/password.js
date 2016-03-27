import {v4} from 'node-uuid';
import {createHmac} from 'crypto';

/**
 * Class representing a hashed Password
 */
export class Password {
    /**
     * Construct the password
     * @param {String} hash The hash of the password
     * @param {String} salt The salt the password was hashed with
     */
    constructor(hash, salt) {
        this._hash = hash;
        this._salt = salt;
    }

    /**
     * Get the hash of the password
     * @return {String} the hash
     */
    get hash() {
        return this._hash;
    }

    /**
     * Get the salt of the password
     * @return {String} the salt
     */
    get salt() {
        return this._salt;
    }

    /**
     * Compare to another password - plaintext or hashd - for equality
     * @param {Password|String} other The password to compare to
     * @return {Boolean} true if the passwords are equal. False if not
     */
    equals(other) {
        let otherPassword;

        if (typeof other === 'string') {
            otherPassword = hashPassword(other, this._salt);
        } else if (other instanceof Password) {
            otherPassword = other;
        } else {
            throw new Error('Unable to compare to this object');
        }
        return this.hash === otherPassword.hash && this.salt === otherPassword.salt;
    }
}

/**
 * Hash a plaintext password, generating a random salt if needed
 * @param {String} password The password to hashPassword
 * @param {String} salt The salt to use. If not provided then a UUID will be generated
 * @return {Password} the hashed password
 */
export function hashPassword(password, salt = v4()) {
    const hash = createHmac('sha256', salt)
        .update(password)
        .digest('hex');
    return new Password(hash, salt);
}
