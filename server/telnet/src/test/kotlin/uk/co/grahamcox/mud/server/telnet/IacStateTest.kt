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
class IacStateTest {
    /** All possible byte values that don't have a special meaning to Telnet */
    fun nonTelnetBytes() = 0.rangeTo(239).toList().map { b -> b.toByte() }

    /**
     * Test decoding any byte that is not special
     * @param b The byte to decode
     */
    @Test
    @Parameters(method = "nonTelnetBytes")
    fun testNonTelnetByte(b: Byte) {
        val result = TelnetDecoderState.IACState.injectByte(b)
        Assert.assertEquals(TelnetDecoderState.NoState, result.newState)
        Assert.assertEquals(TelnetMessage.ByteMessage(b), result.message)
    }

    val SB = 250.toByte()
    val WILL = 251.toByte()
    val WONT = 252.toByte()
    val DO = 253.toByte()
    val DONT = 254.toByte()

    /**
     * Test decoding an IAC byte
     */
    @Test
    fun testIAC() {
        val result = TelnetDecoderState.IACState.injectByte(TelnetBytes.IAC)
        Assert.assertEquals(TelnetDecoderState.NoState, result.newState)
        Assert.assertEquals(TelnetMessage.ByteMessage(TelnetBytes.IAC), result.message)
    }

    /**
     * Test decoding an SE byte
     */
    @Test
    fun testSE() {
        val result = TelnetDecoderState.IACState.injectByte(TelnetBytes.SE)
        Assert.assertEquals(TelnetDecoderState.NoState, result.newState)
        Assert.assertEquals(TelnetMessage.ByteMessage(TelnetBytes.SE), result.message)
    }

    /**
     * Test decoding an NOP byte
     */
    @Test
    fun testNOP() {
        val result = TelnetDecoderState.IACState.injectByte(TelnetBytes.NOP)
        Assert.assertEquals(TelnetDecoderState.NoState, result.newState)
        Assert.assertNull(result.message)
    }

    /**
     * Test decoding any Command byte
     * @param b The byte to decode
     * @param command The expected command
     */
    @Test
    @Parameters(method = "commandParameters", source = TelnetByteParameters::class)
    fun testCommand(b: Byte, command: TelnetMessage.CommandMessage.Command) {
        val result = TelnetDecoderState.IACState.injectByte(b)
        Assert.assertEquals(TelnetDecoderState.NoState, result.newState)
        Assert.assertEquals(TelnetMessage.CommandMessage(command), result.message)
    }

    /**
     * Test decoding an SB byte
     */
    @Test
    fun testSB() {
        val result = TelnetDecoderState.IACState.injectByte(TelnetBytes.SB)
        Assert.assertEquals(TelnetDecoderState.SubnegotiationOptionState, result.newState)
        Assert.assertNull(result.message)
    }

    /**
     * Test decoding a DO byte
     */
    @Test
    fun testDo() =
            testNegotiation(TelnetBytes.DO, TelnetMessage.NegotiationMessage.Negotiation.DO)

    /**
     * Test decoding a DONT byte
     */
    @Test
    fun testDont() =
            testNegotiation(TelnetBytes.DONT, TelnetMessage.NegotiationMessage.Negotiation.DONT)

    /**
     * Test decoding a WILL byte
     */
    @Test
    fun testWill() =
            testNegotiation(TelnetBytes.WILL, TelnetMessage.NegotiationMessage.Negotiation.WILL)

    /**
     * Test decoding a WONT byte
     */
    @Test
    fun testWont() =
            testNegotiation(TelnetBytes.WONT, TelnetMessage.NegotiationMessage.Negotiation.WONT)

    /**
     * Test decoding any Negotiatin byte
     * @param b The byte to decode
     * @param negotiation The negotiation to expect
     */
    private fun testNegotiation(b: Byte, negotiation: TelnetMessage.NegotiationMessage.Negotiation) {
        val result = TelnetDecoderState.IACState.injectByte(b)
        Assert.assertNull(result.message)
        Assert.assertTrue(result.newState is TelnetDecoderState.NegotiationState)

        val newState = result.newState as TelnetDecoderState.NegotiationState
        Assert.assertEquals(negotiation, newState.negotiation)
    }
}
