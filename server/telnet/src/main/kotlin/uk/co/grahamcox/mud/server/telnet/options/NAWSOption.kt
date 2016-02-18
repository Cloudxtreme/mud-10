package uk.co.grahamcox.mud.server.telnet.options

import org.slf4j.LoggerFactory
import uk.co.grahamcox.mud.server.telnet.netty.TelnetOptionHandler

/**
 * Representation of the Negotiate About Window Size option - RFC-1073
 */
class NAWSOption() : TelnetOption() {
    /** The logger to use */
    private val LOG = LoggerFactory.getLogger(NAWSOption::class.java)

    /** The Option ID - 31 */
    override val optionId: Byte = 31

    /** The height of the window */
    var height: Int? = null
        private set

    /** The width of the window */
    var width: Int? = null
        private set

    /**
     * Receive the payload indicating what the window size is now.
     * The payload will always be 4 bytes, structured as W1 W0 H1 H0
     * Where the Width is (W1 * 256) + W0, and the Height is (H1 * 256) + H0
     */
    override fun receiveSubnegotiation(payload: List<Byte>) {
        if (payload.size == 4) {
            width = (unsignByte(payload[0]) * 256) + unsignByte(payload[1])
            height = (unsignByte(payload[2]) * 256) + unsignByte(payload[3])
            LOG.debug("Received new window size of {} x {}", width, height)
        } else {
            LOG.warn("Received invalid payload. Expected 4 bytes but got {}", payload.size)
        }
    }

    private fun unsignByte(b: Byte) : Int = java.lang.Byte.toUnsignedInt(b)
}
