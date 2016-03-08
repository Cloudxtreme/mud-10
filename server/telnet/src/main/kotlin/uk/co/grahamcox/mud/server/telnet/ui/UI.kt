package uk.co.grahamcox.mud.server.telnet.ui

import io.netty.channel.socket.SocketChannel
import org.slf4j.LoggerFactory
import uk.co.grahamcox.mud.server.telnet.options.NAWSOption
import uk.co.grahamcox.mud.server.telnet.options.OptionManager
import uk.co.grahamcox.mud.server.telnet.options.TelnetOption
import uk.co.grahamcox.mud.server.telnet.options.TerminalTypeOption
import java.util.*

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

    /**
     * Enumeration of the status of a Telnet option that we care about
     */
    enum class OptionStatus {
        /** We don't yet know the status */
        UNKNOWN,
        /** The option is disabled */
        DISABLED,
        /** The option expired before we got a response */
        EXPIRED,
        /** The option is enabled */
        ENABLED,
        /** The option is fully configured */
        CONFIGURED
    }

    /** Collection of the Option Statuses that we consider to be terminals. I.e. we now know what we're doing with this option */
    private val TERMINAL_OPTION_STATUSES = EnumSet.of(OptionStatus.DISABLED, OptionStatus.CONFIGURED, OptionStatus.EXPIRED)

    init {
        with(optionManager.getServerOption(NAWSOption::class.java).eventManager) {
            registerListener(TelnetOption.STATE_CHANGED_EVENT) {
                e -> windowSizeStatusChanged(e.payload as TelnetOption.OptionState)
            }
            registerListener(NAWSOption.WINDOW_SIZE_CHANGED_EVENT) {
                e -> windowSizeChanged(e.payload as NAWSOption.WindowSizePayload)
            }
        }

        with(optionManager.getServerOption(TerminalTypeOption::class.java).eventManager) {
            registerListener(TelnetOption.STATE_CHANGED_EVENT) {
                e -> terminalTypeStatusChanged(e.payload as TelnetOption.OptionState)
            }
            registerListener(TerminalTypeOption.TERMINAL_TYPE_CHANGED_EVENT) {
                e -> terminalTypeChanged(e.payload as String)
            }
        }
    }

    /** The status of the Window Size option */
    private var windowSizeOptionStatus: OptionStatus = OptionStatus.UNKNOWN

    /** The actual Window Size to use */
    private var windowSize: NAWSOption.WindowSizePayload? = null

    /** The status of the Terminal Type option */
    private var terminalTypeOptionStatus: OptionStatus = OptionStatus.UNKNOWN

    /** The actual Terminal Type to use */
    private var terminalType: String? = null

    /**
     * Handle the fact that the status of the Window Size option has changed
     * @param newStatus The new status of the Window Size option
     */
    private fun windowSizeStatusChanged(newStatus: TelnetOption.OptionState) {
        windowSizeOptionStatus = when (newStatus) {
            TelnetOption.OptionState.DISABLED -> OptionStatus.DISABLED
            TelnetOption.OptionState.ENABLED -> OptionStatus.ENABLED
            TelnetOption.OptionState.UNKNOWN -> OptionStatus.UNKNOWN
        }
        windowSize = null
        handleOptionChanges()
    }

    /**
     * Handle the fact that the size of the window has changed
     * @param windowSize The new window size
     */
    private fun windowSizeChanged(windowSize: NAWSOption.WindowSizePayload) {
        windowSizeOptionStatus = OptionStatus.CONFIGURED
        this.windowSize = windowSize
        handleOptionChanges()
    }

    /**
     * Handle the fact that the status of the Terminal Type option has changed
     * @param newStatus The new status of the Terminal Type option
     */
    private fun terminalTypeStatusChanged(newStatus: TelnetOption.OptionState) {
        terminalTypeOptionStatus = when (newStatus) {
            TelnetOption.OptionState.DISABLED -> OptionStatus.DISABLED
            TelnetOption.OptionState.ENABLED -> OptionStatus.ENABLED
            TelnetOption.OptionState.UNKNOWN -> OptionStatus.UNKNOWN
        }
        terminalType = null
        handleOptionChanges()
    }

    /**
     * Handle the fact that the terminal type has changed
     * @param terminalType The terminal type
     */
    private fun terminalTypeChanged(terminalType: String) {
        terminalTypeOptionStatus = OptionStatus.CONFIGURED
        this.terminalType = terminalType
        handleOptionChanges()
    }

    /**
     * Handle when the options change.
     * This is primarially used to signal the fact that we know what we're talking about with regards to the terminal
     * and so we can get on with the processing of the game
     */
    private fun handleOptionChanges() {
        LOG.debug("Window Size Status: {}", windowSizeOptionStatus)
        LOG.debug("Window Size: {}", windowSize)
        LOG.debug("Terminal Type Status: {}", terminalTypeOptionStatus)
        LOG.debug("Terminal Type: {}", terminalType)

        if (TERMINAL_OPTION_STATUSES.contains(windowSizeOptionStatus) && TERMINAL_OPTION_STATUSES.contains(terminalTypeOptionStatus)) {
            LOG.debug("Received full configuration of session")
        }
    }
}
