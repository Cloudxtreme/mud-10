package uk.co.grahamcox.mud.users

import uk.co.grahamcox.mud.users.dao.UserDao
import uk.co.grahamcox.mud.users.dao.UserModel

/**
 * Implementation of the User Finder
 */
class UserFinderImpl(private val userDao: UserDao) : UserFinder {
    /**
     * Load the user that has the given ID
     * @param id The ID of the user
     * @return the user
     * @throws UnknownUserException if the requested user doesn't exist
     */
    override fun loadUserById(id: UserId): User {
        throw UnsupportedOperationException()
    }


    /**
     * Find the user that has the given email address
     * @param email The email address to look for
     * @return The user, if found. If no user has this email address then instead return null
     */
    override fun findUserByEmail(email: String): User? =
        userDao.findUserByEmail(email)?.let { translateUser(it) }

    /**
     * Translate a User from the DAO version to the Service version
     * @param user The user to translate
     * @return the translated user
     */
    private fun translateUser(user: UserModel) : User =
            User(
                    id = UserId(user.id),
                    email = user.email,
                    password = Password(hash = user.passwordHash, salt = user.passwordSalt),
                    enabled = user.enabled
            )
}