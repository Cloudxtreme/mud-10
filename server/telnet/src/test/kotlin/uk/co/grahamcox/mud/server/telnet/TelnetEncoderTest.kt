package uk.co.grahamcox.mud.server.telnet

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit tests for the Telnet Encoder
 */
@RunWith(JUnitParamsRunner::class)
class TelnetEncoderTest {
    /**
     * Test encoding byte messages
     */
    @Test
    @Parameters(method = "nonIacBytes", source = TelnetByteParameters::class)
    fun testEncodeByte(byte: Byte) = testEncode(
        TelnetMessage.ByteMessage(byte),
        listOf(byte)
    )

    /**
     * Test encoding byte messages where the byte is an IAC and therefore need to be escaped
     */
    @Test
    fun testEncodeIacByte() = testEncode(
            TelnetMessage.ByteMessage(TelnetBytes.IAC),
            listOf(TelnetBytes.IAC, TelnetBytes.IAC)
    )

    /**
     * Test encoding a command message
     * @param command The command to encode
     * @param commandByte The byte the command encodes to
     */
    @Test
    @Parameters(method = "commandParameters", source = TelnetByteParameters::class)
    fun testEncodeCommand(commandByte: Byte, command: TelnetMessage.CommandMessage.Command) = testEncode(
            TelnetMessage.CommandMessage(command),
            listOf(TelnetBytes.IAC, commandByte)
    )

    /**
     * Test encoding a DO negotiation for an option that is a non-IAC byte
     * @param option The ID of the option to encode
     */
    @Test
    @Parameters(method = "allBytes", source = TelnetByteParameters::class)
    fun testEncodeDoNonIac(option: Byte) =
            testEncodeOptionNegotiation(option, TelnetMessage.NegotiationMessage.Negotiation.DO, TelnetBytes.DO)

    /**
     * Test encoding a DONT negotiation for an option that is a non-IAC byte
     * @param option The ID of the option to encode
     */
    @Test
    @Parameters(method = "allBytes", source = TelnetByteParameters::class)
    fun testEncodeDontNonIac(option: Byte) =
            testEncodeOptionNegotiation(option, TelnetMessage.NegotiationMessage.Negotiation.DONT, TelnetBytes.DONT)

    /**
     * Test encoding a WILL negotiation for an option that is a non-IAC byte
     * @param option The ID of the option to encode
     */
    @Test
    @Parameters(method = "allBytes", source = TelnetByteParameters::class)
    fun testEncodeWillNonIac(option: Byte) =
            testEncodeOptionNegotiation(option, TelnetMessage.NegotiationMessage.Negotiation.WILL, TelnetBytes.WILL)

    /**
     * Test encoding a WONT negotiation for an option that is a non-IAC byte
     * @param option The ID of the option to encode
     */
    @Test
    @Parameters(method = "allBytes", source = TelnetByteParameters::class)
    fun testEncodeWontNonIac(option: Byte) =
            testEncodeOptionNegotiation(option, TelnetMessage.NegotiationMessage.Negotiation.WONT, TelnetBytes.WONT)

    /**
     * Test encoding an Option Negotiation
     * @param option The option ID
     * @param negotiation The negotiation
     * @param negotiationByte The byte representing the option negotiation
     */
    private fun testEncodeOptionNegotiation(option: Byte,
                                            negotiation: TelnetMessage.NegotiationMessage.Negotiation,
                                            negotiationByte: Byte) = testEncode(
            TelnetMessage.NegotiationMessage(negotiation, option),
            listOf(TelnetBytes.IAC, negotiationByte, option))

    /**
     * Test encoding an Option Sub-negotiation where the option ID is a non-IAC byte and the payload is empty
     * @param option The Option ID
     */
    @Test
    @Parameters(method = "allBytes", source = TelnetByteParameters::class)
    fun testEncodeEmptyOptionSubnegotiation(option: Byte) = testEncode(
            TelnetMessage.SubnegotiationMessage(option, listOf()),
            listOf(TelnetBytes.IAC, TelnetBytes.SB, option, TelnetBytes.IAC, TelnetBytes.SE))

    /**
     * Test encoding an Option Sub-negotiation where the option ID is a non-IAC byte and the payload is a single non-IAC byte
     * @param payloadByte The byte to use for the payload
     */
    @Test
    @Parameters(method = "nonIacBytes", source = TelnetByteParameters::class)
    fun testEncodeSingleByteOptionSubnegotiation(payloadByte: Byte) = testEncode(
            TelnetMessage.SubnegotiationMessage(15, listOf(payloadByte)),
            listOf(TelnetBytes.IAC, TelnetBytes.SB, 15, payloadByte, TelnetBytes.IAC, TelnetBytes.SE))

    /**
     * Test encoding an Option Sub-negotiation where the option ID is a non-IAC byte and the payload is a single IAC byte
     */
    @Test
    fun testEncodeSingleIACByteOptionSubnegotiation() = testEncode(
            TelnetMessage.SubnegotiationMessage(15, listOf(TelnetBytes.IAC)),
            listOf(TelnetBytes.IAC, TelnetBytes.SB, 15, TelnetBytes.IAC, TelnetBytes.IAC, TelnetBytes.IAC, TelnetBytes.SE))

    /**
     * Test encoding an Option Sub-negotiation where the option ID is a non-IAC byte and the payload is three bytes
     * long, one of which is an IAC
     */
    @Test
    fun testEncodeMultipleByteOptionSubnegotiation() = testEncode(
            TelnetMessage.SubnegotiationMessage(15, listOf(100, TelnetBytes.IAC, 50)),
            listOf(TelnetBytes.IAC, TelnetBytes.SB, 15, 100, TelnetBytes.IAC, TelnetBytes.IAC, 50, TelnetBytes.IAC, TelnetBytes.SE))

    /**
     * Test the encoder by comparing the encoded version of the provided message to the provided expected output
     * @param message The message to encode
     * @param expected The expected bytes
     */
    private fun testEncode(message: TelnetMessage, expected: List<Byte>) {
        val encoded = TelnetEncoder.encode(message)
        Assert.assertEquals(expected, encoded)
    }
}
