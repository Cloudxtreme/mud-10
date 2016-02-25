package uk.co.grahamcox.mud.server.telnet.ui

import org.slf4j.LoggerFactory
import uk.co.grahamcox.mud.server.telnet.options.NAWSOption
import uk.co.grahamcox.mud.server.telnet.options.OptionManager
import uk.co.grahamcox.mud.server.telnet.options.TerminalTypeOption

/**
 * The actual entrypoint for the user interface
 */
class UI(private val optionManager: OptionManager) {
    /** The logger to use */
    private val LOG = LoggerFactory.getLogger(UI::class.java)

    init {
        optionManager.getServerOption(NAWSOption::class.java).eventManager.registerListener("uk.co.grahamcox.mud.server.telnet.options.NAWSOptionWindowSizeChanged") {
            e -> windowSizeChanged(e.payload as NAWSOption.WindowSizePayload)
        }
        optionManager.getServerOption(TerminalTypeOption::class.java).eventManager.registerListener("uk.co.grahamcox.mud.server.telnet.options.TerminalTypeOptionTerminalTypeChanged") {
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
        LOG.debug("Window size is {}x{}", windowSize?.width, windowSize?.height)
        LOG.debug("Terminal Type is {}", terminalType)
    }
}
