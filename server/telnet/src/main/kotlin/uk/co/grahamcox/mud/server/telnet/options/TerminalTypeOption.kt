package uk.co.grahamcox.mud.server.telnet.options

import io.netty.channel.socket.SocketChannel
import org.slf4j.LoggerFactory
import uk.co.grahamcox.mud.server.telnet.TelnetMessage
import uk.co.grahamcox.mud.server.telnet.netty.TelnetOptionHandler
import java.nio.charset.Charset

/**
 * Representation of the Terminal Type option - RFC-1091
 * @property channel The channel to communicate over
 */
class TerminalTypeOption(private val channel: SocketChannel) : TelnetOption() {
    /** The logger to use */
    private val LOG = LoggerFactory.getLogger(TerminalTypeOption::class.java)

    /** Event name for when the list of terminal names is available */
    val RECEIVED_TERMINAL_NAMES_EVENT = TerminalTypeOption::class.qualifiedName + "ReceivedTerminalNames"

    /** Event name for when the selected terminal type has changed */
    val TERMINAL_TYPE_CHANGED_EVENT = TerminalTypeOption::class.qualifiedName + "TerminalTypeChanged"

    /** The payload for requesting that the client sends the next Terminal Type to the server */
    private val SEND: Byte = 1

    /** The indicator that we've just received a terminal name */
    private val IS: Byte = 0

    /** The Option ID - 24 */
    override val optionId: Byte = 24

    /** The name of the terminal */
    var terminalName: String? = null
        private set

    /** The list of terminal names that we can support */
    private val terminalNamesSupported = arrayListOf<String>()

    /** Whether we've seen all of the terminal names or not */
    private var seenAllTerminalNames = false

    /**
     * If the option has just been enabled then request that the client sends their terminal type to the server
     * @param oldState The old state of the option. Ignored.
     * @param newState The new state of the option
     */
    override fun handleStateChanged(oldState: OptionState, newState: OptionState) {
        if (newState == OptionState.ENABLED) {
            LOG.debug("Option has been enabled. Requesting client terminal type")
            channel.writeAndFlush(TelnetMessage.SubnegotiationMessage(optionId, listOf(SEND)))
        }
    }

    /**
     * Handle receiving a subnegotiation from the client. Ideally this should contain the terminal type to use.
     * @param payload The payload to process
     */
    override fun receiveSubnegotiation(payload: List<Byte>) {
        if (payload.size > 1)
            if (payload[0] == IS) {
                val terminalName = String(payload.toByteArray(), 1, payload.size - 1)
                if (seenAllTerminalNames) {
                    // We've seen all of the possible names, so we're actively selecting one now
                    LOG.debug("Selected terminal name: {}", terminalName)
                    this.terminalName = terminalName
                    eventManager.fire(TERMINAL_TYPE_CHANGED_EVENT, terminalName)
                } else {
                    // We've not seen all of the possible names, so record this as another one
                    LOG.debug("Received terminal name: {}", terminalName)
                    if (terminalNamesSupported.contains(terminalName)) {
                        // We've seen this name before, so it's an indication that we've now seen them all
                        seenAllTerminalNames = true
                        eventManager.fire(RECEIVED_TERMINAL_NAMES_EVENT, terminalNamesSupported as List<String>)
                    }
                    terminalNamesSupported.add(terminalName)
                    channel.writeAndFlush(TelnetMessage.SubnegotiationMessage(optionId, listOf(SEND)))
                }
            } else {
                LOG.warn("Received a payload that did not start with an IS")
        } else {
            LOG.warn("Received a payload that was too short to process")
        }
    }
}
