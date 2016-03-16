package uk.co.grahamcox.mud.users

/**
 * Exception thrown when a user is requested that doesn't exist
 * @param message The error message
 */
class UnknownUserException(message: String) : Exception(message)

/**
 * Interface describing how to find a user record
 */
interface UserFinder {
    /**
     * Load the user that has the given ID
     * @param id The ID of the user
     * @return the user
     * @throws UnknownUserException if the requested user doesn't exist
     *
     */
    fun loadUserById(id: UserId) : User

    /**
     * Find the user that has the given email address
     * @param email The email address to look for
     * @return The user, if found. If no user has this email address then instead return null
     */
    fun findUserByEmail(email: String) : User?
}