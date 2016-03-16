package uk.co.grahamcox.mud.users

/**
 * Representation of a User account
 * @param id The ID of the user
 * @param email The email address of the user
 * @param password The password of the user
 * @param enabled Whether the user account is enabled or not
 */
data class User(val id: UserId,
                val email: String,
                val password: Password,
                val enabled: Boolean)