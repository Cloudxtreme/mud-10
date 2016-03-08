package uk.co.grahamcox.mud.server.telnet.ui

import io.netty.channel.socket.SocketChannel
import org.slf4j.LoggerFactory
import uk.co.grahamcox.mud.server.telnet.options.NAWSOption
import uk.co.grahamcox.mud.server.telnet.options.OptionManager
import uk.co.grahamcox.mud.server.telnet.options.TerminalTypeOption

/**
 * The actual entrypoint for the user interface
 * @param optionManager The option manager to listen to options from
 * @param channel The channel to send messages to
 * @param rendererFactories The renderer factories to work through
 */
class UI(private val optionManager: OptionManager,
         private val channel: SocketChannel) {
    /** The logger to use */
    private val LOG = LoggerFactory.getLogger(UI::class.java)

    init {
        optionManager.getServerOption(NAWSOption::class.java).eventManager.registerListener(NAWSOption.WINDOW_SIZE_CHANGED_EVENT) {
            e -> windowSizeChanged(e.payload as NAWSOption.WindowSizePayload)
        }
        optionManager.getServerOption(TerminalTypeOption::class.java).eventManager.registerListener(TerminalTypeOption.TERMINAL_TYPE_CHANGED_EVENT) {
            e -> terminalTypeChanged(e.payload as String)
        }
    }

    /**
     * Handle the fact that the size of the window has changed
     * @param windowSize The new window size
     */
    private fun windowSizeChanged(windowSize: NAWSOption.WindowSizePayload) {
    }

    /**
     * Handle the fact that the terminal type has changed
     * @param terminalType The terminal type
     */
    private fun terminalTypeChanged(terminalType: String) {
    }
}
