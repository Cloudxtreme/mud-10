package uk.co.grahamcox.mud.server.telnet

import org.slf4j.LoggerFactory

/**
 * Encoder to encode Telnet Messages into a list of Bytes
 */
object TelnetEncoder {
    /** The logger to use */
    private val LOG = LoggerFactory.getLogger(TelnetEncoder::class.java)

    /**
     * Actually encode the given message into a list of bytes
     * @param message The message to encode
     * @reutrn the list of bytes
     */
    fun encode(message: TelnetMessage): List<Byte> {
        LOG.debug("Encoding message {}", message)
        val encoded: List<Byte> = when (message) {
            is TelnetMessage.ByteMessage -> encodeByteMessage(message)
            is TelnetMessage.CommandMessage -> encodeCommandMessage(message)
            is TelnetMessage.NegotiationMessage -> encodeNegotiationMessage(message)
            is TelnetMessage.SubnegotiationMessage -> encodeSubnegotiationMessage(message)
        }
        LOG.debug("Encoded message {} into {}", message, encoded)

        return encoded
    }

    /**
     * Encode a message that represents a single byte.
     * If the single byte is an IAC then this gets escaped.
     * @param message The message to encode
     * @return the encoded message.
     */
    private fun encodeByteMessage(message: TelnetMessage.ByteMessage) = escapeByte(message.byte)

    /**
     * Encode a message that represents a telnet processing command
     * @param message The message to encode
     * @return the encoded message
     */
    private fun encodeCommandMessage(message: TelnetMessage.CommandMessage): List<Byte> {
        val encodedCommand = when(message.command) {
            TelnetMessage.CommandMessage.Command.DATA_MARK -> TelnetBytes.DataMark
            TelnetMessage.CommandMessage.Command.BREAK -> TelnetBytes.Break
            TelnetMessage.CommandMessage.Command.INTERRUPT_PROCESS -> TelnetBytes.InterruptProcess
            TelnetMessage.CommandMessage.Command.ABORT_OUTPUT -> TelnetBytes.AbortOutput
            TelnetMessage.CommandMessage.Command.ARE_YOU_THERE -> TelnetBytes.AreYouThere
            TelnetMessage.CommandMessage.Command.ERASE_CHARACTER -> TelnetBytes.EraseCharacter
            TelnetMessage.CommandMessage.Command.ERASE_LINE -> TelnetBytes.EraseLine
            TelnetMessage.CommandMessage.Command.GO_AHEAD -> TelnetBytes.GoAhead
        }

        return listOf(TelnetBytes.IAC, encodedCommand)
    }

    /**
     * Encode a message that represents a telnet option negotiation
     * @param message The message to encode
     * @return the encoded message
     */
    private fun encodeNegotiationMessage(message: TelnetMessage.NegotiationMessage): List<Byte> {
        val encodedNegotiation = when(message.negotiation) {
            TelnetMessage.NegotiationMessage.Negotiation.DO -> TelnetBytes.DO
            TelnetMessage.NegotiationMessage.Negotiation.DONT -> TelnetBytes.DONT
            TelnetMessage.NegotiationMessage.Negotiation.WILL -> TelnetBytes.WILL
            TelnetMessage.NegotiationMessage.Negotiation.WONT -> TelnetBytes.WONT
        }

        return listOf(TelnetBytes.IAC, encodedNegotiation, message.option)
    }

    /**
     * Encode a message that represents a telnet option subnegotiation
     * @param message The message to encode
     * @return the encoded message
     */
    private fun encodeSubnegotiationMessage(message: TelnetMessage.SubnegotiationMessage) =
        listOf(TelnetBytes.IAC, TelnetBytes.SB, message.option) +
        message.payload.flatMap { b -> escapeByte(b) } +
        listOf(TelnetBytes.IAC, TelnetBytes.SE)

    /**
     * Escape a single byte by adding an IAC in front of it iff it is an IAC itself
     * @param b The byte to escape
     * @return The escaped byte
     */
    private fun escapeByte(b: Byte) = when (b) {
        TelnetBytes.IAC -> listOf(TelnetBytes.IAC, TelnetBytes.IAC)
        else -> listOf(b)
    }
}
