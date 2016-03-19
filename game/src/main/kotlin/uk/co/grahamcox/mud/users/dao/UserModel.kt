package uk.co.grahamcox.mud.users.dao

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.mongojack.ObjectId

/**
 * DAO Model representation of a User
 * @property id The ID of the user
 * @property email The email address of the user
 * @property passwordHash The hash of the users password
 * @property passwordSalt The salt of the users password
 * @property enabled Whether the user is enabled or not
 */
class UserModel
@JsonCreator constructor(
        @ObjectId @JsonProperty("_id") val id: String,
        @JsonProperty("email") val email: String,
        @JsonProperty("passwordHash") val passwordHash: String,
        @JsonProperty("passwordSalt") val passwordSalt: String,
        @JsonProperty("enabled") val enabled: Boolean
)