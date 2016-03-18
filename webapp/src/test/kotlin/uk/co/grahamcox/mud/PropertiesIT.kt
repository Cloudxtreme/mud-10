package uk.co.grahamcox.mud

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

/**
 * Tests for the required properties being set correctly
 */
@RunWith(JUnitParamsRunner::class)
class PropertiesIT {
    /**
     * Test that the properties we want are all set
     */
    @Parameters("host.mud", "host.mongo")
    @Test
    fun testHostPropertiesExist(property: String) {
        Assert.assertNotNull(System.getProperty(property))
    }

    /**
     * Test that the properties we want are all set
     */
    @Parameters("port.mud", "port.mongo")
    @Test
    fun testPortPropertiesExist(property: String) {
        Assert.assertNotNull(System.getProperty(property))
        val portNumber = System.getProperty(property).toInt()
        Assert.assertTrue(portNumber > 0)
        Assert.assertTrue(portNumber < 65536)
    }

    /**
     * Test that we can connect to the containers
     */
    @Parameters(method = "parametersForNetworkConnectivity")
    @Test
    fun testNetworkConnectivity(address: SocketAddress, name: String) {
        val socket = Socket()
        socket.connect(address)
        Assert.assertTrue(socket.isConnected)
        socket.close()
    }

    /**
     * Build the list of containers to connect to
     */
    private fun parametersForNetworkConnectivity() = listOf("mud", "mongo").map { buildConnectionAddress(it) }

    /**
     * Build the address for a single address
     * @param name The name of the address to build
     */
    private fun buildConnectionAddress(name: String) = arrayOf(
            InetSocketAddress(System.getProperty("host.${name}"), System.getProperty("port.${name}").toInt()),
            "${name}"
    )
}
