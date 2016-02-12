package uk.co.grahamcox.mud.server.telnet

/**
 * Storage for some JUnit Parameters for telnet bytes
 */
class TelnetByteParameters {
    /** All possible byte values */
    fun allBytes() = 0.rangeTo(255).toList().map { b -> b.toByte() }

    /** All possible byte values except for IAC */
    fun nonIacBytes() = allBytes().filter { b -> b != TelnetBytes.IAC }
}
