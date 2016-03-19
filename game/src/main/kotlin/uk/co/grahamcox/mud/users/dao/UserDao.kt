package uk.co.grahamcox.mud.users.dao

/**
 * Interface describing the DAO to use for accessing user records
 */
interface UserDao {
    /**
     * Find the user that has the given email address
     * @param email The email address
     * @return the user, if found
     */
    fun findUserByEmail(email: String) : UserModel?
}