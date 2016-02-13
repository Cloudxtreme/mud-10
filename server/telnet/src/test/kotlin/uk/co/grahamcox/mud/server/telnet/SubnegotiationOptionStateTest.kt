package uk.co.grahamcox.mud.server.telnet

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit tests for the Subnegotiation Option State
 */
@RunWith(JUnitParamsRunner::class)
class SubnegotiationOptionStateTest {

    /**
     * Test decoding any byte
     * @param b The byte to decode
     */
    @Test
    @Parameters(method = "allBytes", source = TelnetByteParameters::class)
    fun test(b: Byte) {
        val result = TelnetDecoderState.SubnegotiationOptionState.injectByte(b)
        Assert.assertNull(result.message)
        Assert.assertTrue(result.newState is TelnetDecoderState.SubnegotiationPayloadState)

        val newState = result.newState as TelnetDecoderState.SubnegotiationPayloadState
        Assert.assertEquals(b, newState.option)
        Assert.assertEquals(0, newState.payload.size)
    }
}
