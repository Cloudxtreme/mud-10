package uk.co.grahamcox.mud.users

class UserFinderImpl : UserFinder {
    override fun loadUserById(id: UserId): User {
        throw UnsupportedOperationException()
    }

    override fun findUserByEmail(email: String): User? =
            when (email) {
                "graham@grahamcox.co.uk" -> User(
                        id = UserId("abcdef"),
                        email = email,
                        password = Password.encode("password"),
                        enabled = true
                )
                "banned@grahamcox.co.uk" -> User(
                        id = UserId("abcdef"),
                        email = email,
                        password = Password.encode("password"),
                        enabled = false
                )
                else -> null
            }
}