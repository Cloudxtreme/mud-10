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

    /** The destruction callbacks for the connections */
    private val scopeDestructionCallbacks = hashMapOf<Channel, MutableMap<String, Runnable>>()

    /**
     * Get the map that represents the contents of the current connection
     * @return the scope contents
     */
    private fun getScopeContents(): MutableMap<String, Any> =
            scopeContentsMap.getOrPut(getActiveConnection()) {
                hashMapOf<String, Any>()
            }

    /**
     * Get the list that represents the destruction callbacks of the current connection
     * @param the scope callbacks for the connection
     */
    private fun getScopeDestructionCallbacks(): MutableMap<String, Runnable> =
        scopeDestructionCallbacks.getOrPut(getActiveConnection()) {
            hashMapOf<String, Runnable>()
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

    /**
     * Unregister a connection completely, forgetting all of the registered beans for it
     * @param connection The connection to unregister
     */
    fun unregisterConnection(connection: Channel) {
        LOG.debug("Unregistering conncetion {}", connection)
        clearActiveConnection()
        scopeContentsMap.remove(connection)

        scopeDestructionCallbacks.remove(connection)?.values?.forEach { callback -> callback.run() }
    }

    override fun resolveContextualObject(key: String): Any? = null

    /**
     * Remove the given object from the scope
     * @param key The key of the object to remove
     */
    override fun remove(key: String): Any? {
        val bean = this.getScopeContents().remove(key)
        getScopeDestructionCallbacks().remove(key)?.run()

        return bean
    }

    /**
     * Register a destruction callback for a bean
     * @param key The key of the bean
     * @param callback The callback
     */
    override fun registerDestructionCallback(key: String, callback: Runnable) {
        getScopeDestructionCallbacks().put(key, callback)
    }

    /**
     * Get the ID of the current scope context. This is the ToString of the connection
     * @return the Conversation ID of the scope
     */
    override fun getConversationId(): String = getActiveConnection().toString()

    /**
     * Get a bean from the scope.
     * If this is the specially named bean "channel" then return the currently active channel. Otherwise return the bean
     * from the scope contents with the given name
     * @param key The key of the bean to return
     * @param objectFactory Object Factory to generate the bean if we don't already have an instance of it
     * @return the bean
     */
    override fun get(key: String, objectFactory: ObjectFactory<*>): Any? = when (key) {
        "channel" -> getActiveConnection()
        else -> getScopeContents().getOrPut(key) {
            objectFactory.`object`
        }
    }
}
