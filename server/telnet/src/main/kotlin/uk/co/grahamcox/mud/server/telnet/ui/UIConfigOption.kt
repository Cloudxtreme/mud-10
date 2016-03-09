package uk.co.grahamcox.mud.server.telnet.ui

import uk.co.grahamcox.mud.events.EventManager

/**
 * Interface representing a single configuration option of the underlying UI
 */
abstract class UIConfigOption {
    companion object {
        /** Event name for when the window size changes */
        val STATUS_CHANGED_EVENT = UIConfigOption::class.qualifiedName + "StatusChanged"
    }

    /**
     * Enumeration of the status of a Telnet option that we care about
     */
    enum class OptionStatus {
        /** We don't yet know the status */
        UNKNOWN,
        /** The option is disabled */
        DISABLED,
        /** The option is enabled */
        ENABLED,
        /** The option is fully configured */
        CONFIGURED
    }

    /** The status of the Window Size option */
    var optionStatus: OptionStatus = OptionStatus.UNKNOWN
        protected set(value) {
            if (field != value) {
                field = value
                eventManager.fire(STATUS_CHANGED_EVENT, value)
            }
        }

    /** Event Manager used to signal changes in configuration */
    val eventManager = EventManager()
}
