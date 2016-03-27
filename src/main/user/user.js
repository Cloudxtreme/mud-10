/**
 * Representation of a User account
 */
export class User {
    /**
     * Construct the User
     * @param {String} id The User ID
     * @param {String} email The users email address
     * @param {Password} password The users password
     * @param {Boolean} enabled Whether the user is enabled or not
     */
    constructor (id, email, password, enabled) {
        this._id = id;
        this._email = email;
        this._password = password;
        this._enabled = enabled;
    }

    /**
     * Get the User ID
     * @return {String} the User ID
     */
    get id() {
        return this._id;
    }

    /**
     * Get the Enail Address
     * @return {String} the Enail Address
     */
    get email() {
        return this._email;
    }

    /**
     * Get the Users Password
     * @return {Password} the Users Password
     */
    get password() {
        return this._password;
    }

    /**
     * Get if the user is enabled or not
     * @return {Boolean} true if the user is enabled. False if not
     */
    get enabled() {
        return this._enabled;
    }
}
