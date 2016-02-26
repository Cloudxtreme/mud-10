package uk.co.grahamcox.mud.server.telnet.options

import org.slf4j.LoggerFactory
import uk.co.grahamcox.mud.server.telnet.netty.TelnetOptionHandler

/**
 * Representation of the Negotiate About Window Size option - RFC-1073
 */
class NAWSOption() : TelnetOption() {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(NAWSOption::class.java)

        /** Event name for when the window size changes */
        val WINDOW_SIZE_CHANGED_EVENT = NAWSOption::class.qualifiedName + "WindowSizeChanged"
    }

    /**
     * Representation of the window size as a payload in the Window Size Changed Event
     * @property width The width of the window
     * @property height The height of the window
     */
    data class WindowSizePayload(val width: Int, val height: Int)

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
            val width = (unsignByte(payload[0]) * 256) + unsignByte(payload[1])
            val height = (unsignByte(payload[2]) * 256) + unsignByte(payload[3])

            this.width = width
            this.height = height

            LOG.debug("Received new window size of {} x {}", width, height)
            eventManager.fire(WINDOW_SIZE_CHANGED_EVENT, WindowSizePayload(height, width))
        } else {
            LOG.warn("Received invalid payload. Expected 4 bytes but got {}", payload.size)
        }
    }

    private fun unsignByte(b: Byte) : Int = java.lang.Byte.toUnsignedInt(b)
}
