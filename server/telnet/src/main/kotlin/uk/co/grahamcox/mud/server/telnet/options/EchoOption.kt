package uk.co.grahamcox.mud.server.telnet.options

/**
 * Representation of the Echo option - RFC-857
 */
class EchoOption() : TelnetOption() {
    /** The Option ID - 1 */
    override val optionId: Byte = 1
}
