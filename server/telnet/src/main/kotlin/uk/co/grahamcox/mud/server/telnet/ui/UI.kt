package uk.co.grahamcox.mud.server.telnet.ui

import io.netty.channel.socket.SocketChannel
import org.slf4j.LoggerFactory
import uk.co.grahamcox.mud.server.telnet.TelnetMessage
import uk.co.grahamcox.mud.server.telnet.options.NAWSOption
import uk.co.grahamcox.mud.server.telnet.options.OptionManager
import uk.co.grahamcox.mud.server.telnet.options.TerminalTypeOption

/**
 * The actual entrypoint for the user interface
 * @param optionManager The option manager to listen to options from
 * @param channel The channel to send messages to
 */
class UI(private val optionManager: OptionManager, private val channel: SocketChannel) {
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

    /** The size of the window as we currently know it */
    private var windowSize: NAWSOption.WindowSizePayload? = null

    /** The terminal type as we currently know it */
    private var terminalType: String? = null

    /**
     * Handle the fact that the size of the window has changed
     * @param windowSize The new window size
     */
    private fun windowSizeChanged(windowSize: NAWSOption.WindowSizePayload) {
        this.windowSize = windowSize
        renderUI()
    }

    /**
     * Handle the fact that the terminal type has changed
     * @param terminalType The terminal type
     */
    private fun terminalTypeChanged(terminalType: String) {
        this.terminalType = terminalType
        renderUI()
    }

    /**
     * Actually render the UI as it currently is
     */
    private fun renderUI() {
        val ESC: Char = 27.toByte().toChar()
        "${ESC}[2J${ESC}[0;0H".toByteArray()
            .map { b -> TelnetMessage.ByteMessage(b) }
            .forEach { m -> channel.write(m) }
        "Window size is ${windowSize?.width}x${windowSize?.height}\n\r".toByteArray()
            .map { b -> TelnetMessage.ByteMessage(b) }
            .forEach { m -> channel.write(m) }
        "Terminal Type is ${terminalType}\n\r".toByteArray()
            .map { b -> TelnetMessage.ByteMessage(b) }
            .forEach { m -> channel.write(m) }
        channel.flush()
    }
}
