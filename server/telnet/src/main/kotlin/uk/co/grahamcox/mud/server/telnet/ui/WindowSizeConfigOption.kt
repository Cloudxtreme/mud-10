package uk.co.grahamcox.mud.server.telnet.ui

import org.slf4j.LoggerFactory
import uk.co.grahamcox.mud.server.telnet.options.NAWSOption
import uk.co.grahamcox.mud.server.telnet.options.OptionManager
import uk.co.grahamcox.mud.server.telnet.options.TelnetOption

/**
 * Representation of the configuration option for knowing the window size
 * @param optionManager The option manager to get the NAWS Option from
 */
class WindowSizeConfigOption(val optionManager: OptionManager) : UIConfigOption() {
    /** The logger to use */
    private val LOG = LoggerFactory.getLogger(WindowSizeConfigOption::class.java)

    init {
        with(optionManager.getServerOption(NAWSOption::class.java).eventManager) {
            registerListener(TelnetOption.STATE_CHANGED_EVENT) {
                e -> windowSizeStatusChanged(e.payload as TelnetOption.OptionState)
            }
            registerListener(NAWSOption.WINDOW_SIZE_CHANGED_EVENT) {
                e -> windowSizeChanged(e.payload as NAWSOption.WindowSizePayload)
            }
        }
    }

    /** The actual Window Size to use */
    var windowSize: NAWSOption.WindowSizePayload? = null

    /**
     * Handle the fact that the status of the Window Size option has changed
     * @param newStatus The new status of the Window Size option
     */
    private fun windowSizeStatusChanged(newStatus: TelnetOption.OptionState) {
        LOG.debug("NAWS Telnet Option has changed status: {}", newStatus)
        windowSize = null
        optionStatus = when (newStatus) {
            TelnetOption.OptionState.DISABLED -> OptionStatus.DISABLED
            TelnetOption.OptionState.ENABLED -> OptionStatus.ENABLED
            TelnetOption.OptionState.UNKNOWN -> OptionStatus.UNKNOWN
        }
    }

    /**
     * Handle the fact that the size of the window has changed
     * @param windowSize The new window size
     */
    private fun windowSizeChanged(windowSize: NAWSOption.WindowSizePayload) {
        LOG.debug("NAWS Telnet Option has informed us of the new window size")
        this.windowSize = windowSize
        optionStatus = OptionStatus.CONFIGURED
    }
}
