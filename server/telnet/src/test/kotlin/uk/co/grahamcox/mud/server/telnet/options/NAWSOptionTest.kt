package uk.co.grahamcox.mud.server.telnet.options

import org.junit.Assert
import org.junit.Test

class NAWSOptionTest {
    /**
     * Test decoding a valid payload where the numbers are not too large for a signed byte
     */
    @Test
    fun testDecodeValidSmallPayload() {
        val option = NAWSOption()
        option.receiveSubnegotiation(listOf(0, 81, 0, 71))
        Assert.assertEquals(81, option.width)
        Assert.assertEquals(71, option.height)
    }

    /**
     * Test decoding a valid payload where the numbers are too large for a signed byte.
     * Essentially this tests the fact that the bytes are to be treated as unsigned bytes
     */
    @Test
    fun testDecodeValidLargePayload() {
        val option = NAWSOption()
        option.receiveSubnegotiation(listOf(0, 181.toByte(), 0, 171.toByte()))
        Assert.assertEquals(181, option.width)
        Assert.assertEquals(171, option.height)
    }

    /**
     * Test decoding a payload that is shorter than 4 bytes
     */
    @Test
    fun testDecodePayloadTooShort() {
        val option = NAWSOption()
        option.receiveSubnegotiation(listOf(0, 81))
        Assert.assertNull(option.width)
        Assert.assertNull(option.height)
    }

    /**
     * Test decoding a payload that is longer than 4 bytes
     */
    @Test
    fun testDecodePayloadTooLong() {
        val option = NAWSOption()
        option.receiveSubnegotiation(listOf(0, 81, 0, 81, 0, 81))
        Assert.assertNull(option.width)
        Assert.assertNull(option.height)
    }
}
