package uk.co.grahamcox.mud.server.telnet.spring

import io.netty.channel.Channel
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.config.Scope

/**
 * Spring Scope for all beans that belong to the connection
 */
class ConnectionScope : Scope {
    companion object {
        private val LOG = LoggerFactory.getLogger(ConnectionScope::class.java)
    }

    /** The connections that are active on each thread */
    private val activeConnections = ThreadLocal<Channel>()

    /** The actual scope contents */
    private val scopeContentsMap = hashMapOf<Channel, MutableMap<String, Any>>()

    /**
     * Get the map that represents the contents of the current scope
     * @return the scope contents
     */
    private fun getScopeContents(): MutableMap<String, Any> =
            scopeContentsMap.getOrPut(getActiveConnection()) {
                hashMapOf<String, Any>()
            }

    /**
     * Get the connection that is currently active, if there is one
     * @return the currently active connection
     * @throws IllegalStateException if no connection is currently active
     */
    private fun getActiveConnection() =
            activeConnections.get() ?: throw IllegalStateException("No currently active connection on this thread")

    /**
     * Set the connection that is currently active for this thread
     * @param connection The connection to use
     */
    fun setActiveConnection(connection: Channel) = activeConnections.set(connection)

    /**
     * Clear the connection that is currently active for this thread
     */
    fun clearActiveConnection() = activeConnections.remove()

    override fun resolveContextualObject(key: String): Any? = null

    override fun remove(key: String): Any? = this.getScopeContents().remove(key)

    override fun registerDestructionCallback(key: String, callback: Runnable?) {
        LOG.warn("ConnectionScope does not support destruction callbacks.")
    }

    override fun getConversationId(): String = getActiveConnection().toString()

    override fun get(key: String, objectFactory: ObjectFactory<*>): Any? = when (key) {
        "connectionScope" -> this
        "channel" -> getActiveConnection()
        else -> getScopeContents().getOrPut(key) {
            objectFactory.`object`
        }
    }
}
