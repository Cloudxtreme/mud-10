package uk.co.grahamcox.mud.server.telnet

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit tests for the Negotiation State
 */
@RunWith(JUnitParamsRunner::class)
class NegotiationStateTest {

    /**
     * Test decoding any option for a DO negotiation
     * @param b The byte to decode
     */
    @Test
    @Parameters(method = "allBytes", source = TelnetByteParameters::class)
    fun testDo(b: Byte) {
        val state = TelnetDecoderState.NegotiationState(TelnetMessage.NegotiationMessage.Negotiation.DO)
        val result = state.injectByte(b)
        Assert.assertEquals(TelnetDecoderState.NoState, result.newState)
        Assert.assertEquals(TelnetMessage.NegotiationMessage(TelnetMessage.NegotiationMessage.Negotiation.DO, b), result.message)
    }
}
