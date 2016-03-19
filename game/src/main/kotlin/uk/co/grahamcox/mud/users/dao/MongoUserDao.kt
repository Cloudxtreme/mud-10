package uk.co.grahamcox.mud.users.dao

import org.mongojack.DBQuery
import org.mongojack.JacksonDBCollection
import uk.co.grahamcox.mud.users.Password

/**
 * MongoDB implementation of the User DAO
 */
class MongoUserDao(private val userCollection: JacksonDBCollection<UserModel, String>) : UserDao {
    /**
     * Find the user that has the given email address
     * @param email The email address
     * @return the user, if found
     */
    override fun findUserByEmail(email: String): UserModel? =
            userCollection.findOne(DBQuery.`is`("email", email))
}