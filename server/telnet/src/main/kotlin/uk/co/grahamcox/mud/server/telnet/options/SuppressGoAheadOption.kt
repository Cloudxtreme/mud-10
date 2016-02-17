package uk.co.grahamcox.mud.server.telnet.options

/**
 * Representation of the Suppress Go Ahead option - RFC-858
 */
class SuppressGoAheadOption() : TelnetOption() {
    /** The Option ID - 3 */
    override val optionId: Byte = 3
}
