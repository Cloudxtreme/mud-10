package uk.co.grahamcox.mud.users

import org.junit.Assert
import org.junit.Test

/**
 * Unit tests for the Password class
 */
class PasswordTest {
    /**
     * Test encoding a password with a provided salt
     */
    @Test
    fun testEncode() {
        val encoded = Password.encode("password", "salt")
        Assert.assertEquals("salt", encoded.salt)
        Assert.assertEquals("E2Ab2k6njlWge5iGbSvmvgdE44ZvE8AMgRyrYIoo8yI=", encoded.hash)
    }

    /**
     * Test comparing the password to another password object
     */
    @Test
    fun testEqualsPassword() {
        val encoded = Password.encode("password", "salt")
        Assert.assertTrue(encoded.equals(Password.encode("password", "salt")))
        Assert.assertFalse(encoded.equals(Password.encode("password", "pepper")))
        Assert.assertFalse(encoded.equals(Password.encode("password", "Salt")))
        Assert.assertFalse(encoded.equals(Password.encode("wrong", "salt")))
        Assert.assertFalse(encoded.equals(Password.encode("Password", "salt")))
    }

    /**
     * Test comparing the password to another password object
     */
    @Test
    fun testEqualsString() {
        val encoded = Password.encode("password", "salt")
        Assert.assertTrue(encoded.equals("password"))
        Assert.assertFalse(encoded.equals("wrong"))
        Assert.assertFalse(encoded.equals("Password"))
    }
}