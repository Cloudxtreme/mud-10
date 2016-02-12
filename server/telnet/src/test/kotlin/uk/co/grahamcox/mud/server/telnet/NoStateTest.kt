package uk.co.grahamcox.mud.server.telnet

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit tests for the No State
 */
@RunWith(JUnitParamsRunner::class)
class NoStateTest {

    /**
     * Test decoding any byte that is not an IAC
     * @param b The byte to decode
     */
    @Test
    @Parameters(method = "nonIacBytes", source = TelnetByteParameters::class)
    fun testNonIacByte(b: Byte) {
        val result = TelnetDecoderState.NoState.injectByte(b)
        Assert.assertEquals(TelnetDecoderState.NoState, result.newState)
        Assert.assertEquals(TelnetMessage.ByteMessage(b), result.message)
    }

    /**
     * Test decoding an IAC byte
     */
    @Test
    fun testIacByte() {
        val result = TelnetDecoderState.NoState.injectByte(TelnetBytes.IAC)
        Assert.assertEquals(TelnetDecoderState.IACState, result.newState)
        Assert.assertNull(result.message)
    }
}
