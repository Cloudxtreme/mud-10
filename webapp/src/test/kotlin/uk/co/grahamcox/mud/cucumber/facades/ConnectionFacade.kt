package uk.co.grahamcox.mud.cucumber.facades

import org.slf4j.LoggerFactory
import uk.co.grahamcox.mud.cucumber.Context

/**
 * Facade to allow us to connect to the MUD
 */
class ConnectionFacade(private val context: Context) {
    /** The logger to use */
    private val LOG = LoggerFactory.getLogger(ConnectionFacade::class.java)
    /**
     * Connect to the mud on the system property defined connection details
     */
    fun connect() {
        val host = System.getProperty("host.mud") ?: throw IllegalArgumentException("System property host.mud was not defined")
        val port = System.getProperty("port.mud")?.toInt() ?: throw IllegalArgumentException("System property port.mud was not defined")

        connect(host, port)
    }

    /**
     * Connect to the mud on the provided host and port
     * @param host The host to connect to
     * @param port The port to connect to
     */
    fun connect(host: String, port: Int) {
        if (context.connection != null) {
            throw IllegalStateException("We are already connected")
        }
        LOG.info("Connecting to mud on telnet://{}:{}", host, port)
        context.connection = host + port

    }
}
