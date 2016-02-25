package uk.co.grahamcox.mud.server.telnet.options

import uk.co.grahamcox.mud.events.EventManager
import uk.co.grahamcox.mud.server.telnet.TelnetMessage

/**
 * Representation of a Telnet Option that we support
 */
abstract class TelnetOption {
    /** Event name for when the state of the option changes */
    val STATE_CHANGED_EVENT = TelnetOption::class.qualifiedName + "StateChanged"

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

    /** The mechanism to trigger events */
    val eventManager = EventManager()

    /** The state of the option as requested by the client */
    var clientState: TelnetMessage.NegotiationMessage.Negotiation? = null
        set(value) {
            val oldState = state
            field = value
            val newState = state
            if (oldState != newState) {
                stateChanged(oldState, newState)
            }
        }

    /** The state of the option as requested by the server */
    var serverState: TelnetMessage.NegotiationMessage.Negotiation? = null
        set(value) {
            val oldState = state
            field = value
            val newState = state
            if (oldState != newState) {
                stateChanged(oldState, newState)
            }
        }

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
     * Receive the payload from a Subnegotiation message
     * @param payload The payload to process
     */
    open fun receiveSubnegotiation(payload: List<Byte>) {}

    /**
     * Handle the fact that the state of the option has just changed
     * @param oldState The old state of the option
     * @param newState The new state of the option
     */
    private fun stateChanged(oldState: OptionState, newState: OptionState) {
        handleStateChanged(oldState, newState)
        eventManager.fire(STATE_CHANGED_EVENT, newState)
    }

    /**
     * Handle the fact that the state of the option has just changed
     * @param oldState The old state of the option
     * @param newState The new state of the option
     */
    open protected fun handleStateChanged(oldState: OptionState, newState: OptionState) {}

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
