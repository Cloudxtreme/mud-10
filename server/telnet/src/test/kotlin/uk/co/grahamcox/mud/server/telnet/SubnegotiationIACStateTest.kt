package uk.co.grahamcox.mud.server.telnet

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit tests for the Subnegotiation IAC State
 */
@RunWith(JUnitParamsRunner::class)
class SubnegotiationIACStateTest {

    /** All possible byte values except for SE */
    fun nonSEBytes() = TelnetByteParameters().allBytes().filter { b -> b != TelnetBytes.SE }

    /**
     * Test decoding any byte that is not an SE
     * @param b The byte to decode
     */
    @Test
    @Parameters(method = "nonSEBytes")
    fun testNonSEByte(b: Byte) {
        val state = TelnetDecoderState.SubnegotiationIACState(15, listOf(1, 2, 3))
        val result = state.injectByte(b)
        Assert.assertTrue(result.newState is TelnetDecoderState.SubnegotiationIACState)

        val newState = result.newState as TelnetDecoderState.SubnegotiationIACState
        Assert.assertEquals(15.toByte(), newState.option)
        Assert.assertEquals(listOf(1, 2, 3, b), newState.payload)
        Assert.assertNull(result.message)
    }

    /**
     * Test decoding an SE byte
     */
    @Test
    fun testSEByte() {
        val state = TelnetDecoderState.SubnegotiationIACState(15, listOf(1, 2, 3))
        val result = state.injectByte(TelnetBytes.SE)
        Assert.assertEquals(TelnetDecoderState.NoState, result.newState)
        Assert.assertEquals(TelnetMessage.SubnegotiationMessage(15, listOf(1, 2, 3)), result.message)
    }
}
