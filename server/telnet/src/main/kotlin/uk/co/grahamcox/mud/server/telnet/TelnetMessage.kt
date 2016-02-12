package uk.co.grahamcox.mud.server.telnet

/**
 * Base class for all of the possible Telnet Messages that can be supported
 */
sealed class TelnetMessage {
    /**
     * Telnet message representing a single byte that was sent or received
     * @property byte The actual byte
     */
    class ByteMessage(val byte: Byte) : TelnetMessage() {
        /**
         * Compare to another object for equality
         * @param other The other object to compare to
         * @return True if the two are equal. False if not
         */
        override fun equals(other: Any?): Boolean{
            if (this === other) return true
            if (other?.javaClass != javaClass) return false

            other as ByteMessage

            if (byte != other.byte) return false

            return true
        }

        /**
         * Generate a hashcode for the message
         * @return the hashcode
         */
        override fun hashCode(): Int{
            return byte as Int
        }
    }

    /**
     * Telnet message representing a Command that was sent or received
     * @property command The actual command
     */
    class CommandMessage(val command: Command) : TelnetMessage() {
        /**
         * Enumeration of the supported commands
         */
        enum class Command {
            /** The Data Mark command */
            DATA_MARK,
            /** The Break command */
            BREAK,
            /** The Interrupt Process command */
            INTERRUPT_PROCESS,
            /** The Abort Output command */
            ABORT_OUTPUT,
            /** The Are You There command */
            ARE_YOU_THERE,
            /** The Erase Character command */
            ERASE_CHARACTER,
            /** The Erase Line command */
            ERASE_LINE,
            /** The Go Ahead command */
            GO_AHEAD
        }
    }

    /**
     * Telnet message representing an option negotiation
     * @property negotiation The negotiation to perform
     * @property option The option to negotiate
     */
    class NegotiationMessage(val negotiation: Negotiation, val option: Byte) : TelnetMessage() {
        /**
         * Enumeration of the supported negotiations
         */
        enum class Negotiation {
            /** The DO Negotiation */
            DO,
            /** The DONT Negotiation */
            DONT,
            /** The WILL Negotiation */
            WILL,
            /** The WONT Negotiation */
            WONT
        }
    }

    /**
     * Telnet message representing an option sub-negotiation
     * @property option The option to sub-negotiate
     * @property payload The payload of the sub-negotiation
     */
    class SubnegotiationMessage(val option: Byte, val payload: List<Byte>) : TelnetMessage()
}
