package uk.co.grahamcox.mud.server.telnet

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit tests for the Telnet Decoder
 */
@RunWith(JUnitParamsRunner::class)
class TelnetDecoderTest {
    /**
     * Test decoding a stream of input bytes and that the correct output messages are produced
     * @param input The input bytes
     * @param expected The expected telnet messages
     */
    @Test
    @Parameters
    fun testDecoder(input: List<Byte>, expected: List<TelnetMessage>) {
        val decoder = TelnetDecoder()
        val produced = input
                .map { b -> decoder.inject(b) }
                .filterNotNull()
        Assert.assertEquals(expected, produced)
    }

    /**
     * Generate all of the parameters that represent a single unescaped byte
     */
    fun singleByteParameters() = TelnetByteParameters().nonIacBytes().map { b ->
        arrayOf(
            listOf(b),
            listOf(TelnetMessage.ByteMessage(b))
        )
    }

    /**
     * Generate a parameter that represents all of the non-IAC bytes together
     */
    fun allByteParameters() = listOf(
        arrayOf(
            TelnetByteParameters().nonIacBytes(),
            TelnetByteParameters().nonIacBytes().map { b -> TelnetMessage.ByteMessage(b) }
        )
    )

    /**
     * Generate all of the parameters that represent a Command mapping
     */
    fun commandParameters() = TelnetByteParameters().commandByteMapping().map { entry ->
        arrayOf(
            listOf(TelnetBytes.IAC, entry.key),
            listOf(TelnetMessage.CommandMessage(entry.value))
        )
    }

    /**
     * Generate a parameter that represents every possible command in a single buffer
     */
    fun allCommandParameters() = listOf(
        arrayOf(
            TelnetByteParameters().commandByteMapping().flatMap { entry -> listOf(TelnetBytes.IAC, entry.key) },
            TelnetByteParameters().commandByteMapping().map { entry -> TelnetMessage.CommandMessage(entry.value) }
        )
    )

    /**
     * Generate all of the parameters that represent every possible negotiation
     */
    fun negotiationParameters() = TelnetByteParameters().negotiationByteMapping().flatMap { entry ->
        TelnetByteParameters().allBytes().map { option ->
            arrayOf(
                listOf(TelnetBytes.IAC, entry.key, option),
                listOf(TelnetMessage.NegotiationMessage(entry.value, option))
            )
        }
    }

    /**
     * Generate a parameter for a Subnegotiation
     */
    fun subnegotiationParameters() = listOf(
        arrayOf(
            listOf(TelnetBytes.IAC, TelnetBytes.SB, 15, 1, 2, TelnetBytes.IAC, TelnetBytes.IAC, TelnetBytes.IAC, TelnetBytes.SE),
            listOf(TelnetMessage.SubnegotiationMessage(15, listOf(1, 2, TelnetBytes.IAC)))
        )
    )

    /**
     * Produce the parameters for the actual test
     * @return the parameters. This is a list of pairs of lists
     */
    fun parametersForTestDecoder() = singleByteParameters() +
        commandParameters() +
        negotiationParameters() +
        subnegotiationParameters() +
        allByteParameters() +
        allCommandParameters()
}
