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
class SubnegotiationPayloadStateTest {

    /**
     * Test decoding any byte that is not an IAC
     * @param b The byte to decode
     */
    @Test
    @Parameters(method = "nonIacBytes", source = TelnetByteParameters::class)
    fun testNonIACByte(b: Byte) {
        val state = TelnetDecoderState.SubnegotiationPayloadState(15, listOf(1, 2, 3))
        val result = state.injectByte(b)
        Assert.assertTrue(result.newState is TelnetDecoderState.SubnegotiationPayloadState)

        val newState = result.newState as TelnetDecoderState.SubnegotiationPayloadState
        Assert.assertEquals(15.toByte(), newState.option)
        Assert.assertEquals(listOf(1, 2, 3, b), newState.payload)
        Assert.assertNull(result.message)
    }

    /**
     * Test decoding an IAC byte
     */
    @Test
    fun testIACByte() {
        val state = TelnetDecoderState.SubnegotiationPayloadState(15, listOf(1, 2, 3))
        val result = state.injectByte(TelnetBytes.IAC)
        Assert.assertTrue(result.newState is TelnetDecoderState.SubnegotiationIACState)

        val newState = result.newState as TelnetDecoderState.SubnegotiationIACState
        Assert.assertEquals(15.toByte(), newState.option)
        Assert.assertEquals(listOf<Byte>(1, 2, 3), newState.payload)
        Assert.assertNull(result.message)
    }
}
