package uk.co.grahamcox.mud.server.telnet.options

import uk.co.grahamcox.mud.server.telnet.TelnetMessage

/**
 * Representation of a Telnet Option that we support
 */
abstract class TelnetOption {
    /** The state of the option */
    enum class OptionState {
        /** The option is enabled */
        ENABLED,
        /** The option is disabled */
        DISABLED,
        /** the option is still unknown */
        UNKNOWN
    }
    /** The ID of the option */
    abstract val optionId: Byte

    /** The state of the option as requested by the client */
    var clientState: TelnetMessage.NegotiationMessage.Negotiation? = null

    /** The state of the option as requested by the server */
    var serverState: TelnetMessage.NegotiationMessage.Negotiation? = null

    /** The current state of the option */
    val state: OptionState
        get() = if (isEnabled()) {
            OptionState.ENABLED
        } else if (isDisabled()) {
            OptionState.DISABLED
        } else {
            OptionState.UNKNOWN
        }

    /**
     * Check if the option has been enabled. This means that exactly one of the clientState and serverState is set to DO, and the other is set to WILL
     * @return true if the option is enabled. False if not
     */
    private fun isEnabled() = (clientState == TelnetMessage.NegotiationMessage.Negotiation.DO && serverState == TelnetMessage.NegotiationMessage.Negotiation.WILL) ||
            (clientState == TelnetMessage.NegotiationMessage.Negotiation.WILL && serverState == TelnetMessage.NegotiationMessage.Negotiation.DO)

    /**
     * Check if the option is disabled. This means that either the Client or the Server State is set to ether DONT or WONT
     * @return true if the option is disabled. False if not
     */
    private fun isDisabled() = clientState == TelnetMessage.NegotiationMessage.Negotiation.DONT ||
            clientState == TelnetMessage.NegotiationMessage.Negotiation.WONT ||
            serverState == TelnetMessage.NegotiationMessage.Negotiation.DONT ||
            serverState == TelnetMessage.NegotiationMessage.Negotiation.WONT
}
