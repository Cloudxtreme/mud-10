package uk.co.grahamcox.mud.server.telnet

/**
 * Storage for some JUnit Parameters for telnet bytes
 */
class TelnetByteParameters {
    /** All possible byte values */
    fun allBytes() = 0.rangeTo(255).toList().map { b -> b.toByte() }

    /** All possible byte values except for IAC */
    fun nonIacBytes() = allBytes().filter { b -> b != TelnetBytes.IAC }

    /** Mapping of Telnet Bytes to Commands */
    fun commandByteMapping() = mapOf(
            TelnetBytes.DataMark to TelnetMessage.CommandMessage.Command.DATA_MARK,
            TelnetBytes.Break to TelnetMessage.CommandMessage.Command.BREAK,
            TelnetBytes.InterruptProcess to TelnetMessage.CommandMessage.Command.INTERRUPT_PROCESS,
            TelnetBytes.AbortOutput to TelnetMessage.CommandMessage.Command.ABORT_OUTPUT,
            TelnetBytes.AreYouThere to TelnetMessage.CommandMessage.Command.ARE_YOU_THERE,
            TelnetBytes.EraseCharacter to TelnetMessage.CommandMessage.Command.ERASE_CHARACTER,
            TelnetBytes.EraseLine to TelnetMessage.CommandMessage.Command.ERASE_LINE,
            TelnetBytes.GoAhead to TelnetMessage.CommandMessage.Command.GO_AHEAD
    )

    /**
     * Parameters for the tests to encode commands
     */
    fun commandParameters() = commandByteMapping().map { entry ->
        arrayOf(entry.key, entry.value)
    }

    /** Mapping of Telnet Bytes to Negotiations */
    fun negotiationByteMapping() = mapOf(
            TelnetBytes.DO to TelnetMessage.NegotiationMessage.Negotiation.DO,
            TelnetBytes.DONT to TelnetMessage.NegotiationMessage.Negotiation.DONT,
            TelnetBytes.WILL to TelnetMessage.NegotiationMessage.Negotiation.WILL,
            TelnetBytes.WONT to TelnetMessage.NegotiationMessage.Negotiation.WONT
    )
}
