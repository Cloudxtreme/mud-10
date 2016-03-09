package uk.co.grahamcox.mud.server.telnet.ui

import org.slf4j.LoggerFactory
import uk.co.grahamcox.mud.server.telnet.options.NAWSOption
import uk.co.grahamcox.mud.server.telnet.options.OptionManager
import uk.co.grahamcox.mud.server.telnet.options.TelnetOption
import uk.co.grahamcox.mud.server.telnet.options.TerminalTypeOption

/**
 * Representation of the configuration option for knowing the Terminal Type
 * @param optionManager The option manager to get the Terminal Type Option from
 */
class TerminalTypeConfigOption(val optionManager: OptionManager) : UIConfigOption() {
    /** The logger to use */
    private val LOG = LoggerFactory.getLogger(TerminalTypeConfigOption::class.java)

    init {
        with(optionManager.getServerOption(TerminalTypeOption::class.java).eventManager) {
            registerListener(TelnetOption.STATE_CHANGED_EVENT) {
                e -> terminalTypeStatusChanged(e.payload as TelnetOption.OptionState)
            }
            registerListener(TerminalTypeOption.TERMINAL_TYPE_CHANGED_EVENT) {
                e -> terminalTypeChanged(e.payload as String)
            }
        }
    }

    /** The actual Terminal Type to use */
    private var terminalType: String? = null

    /**
     * Handle the fact that the status of the Window Size option has changed
     * @param newStatus The new status of the Window Size option
     */
    private fun terminalTypeStatusChanged(newStatus: TelnetOption.OptionState) {
        LOG.debug("Terminal Type Telnet Option has changed status: {}", newStatus)
        terminalType = null
        optionStatus = when (newStatus) {
            TelnetOption.OptionState.DISABLED -> OptionStatus.DISABLED
            TelnetOption.OptionState.ENABLED -> OptionStatus.ENABLED
            TelnetOption.OptionState.UNKNOWN -> OptionStatus.UNKNOWN
        }
    }

    /**
     * Handle the fact that the size of the terminal type has changed
     * @param windowSize The new terminal type
     */
    private fun terminalTypeChanged(terminalType: String) {
        LOG.debug("Terminal Type Telnet Option has informed us of a selected terminal type: {}", terminalType)
        this.terminalType = terminalType
        optionStatus = OptionStatus.CONFIGURED
    }
}
