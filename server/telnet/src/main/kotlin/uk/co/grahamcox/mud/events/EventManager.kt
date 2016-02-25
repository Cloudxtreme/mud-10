package uk.co.grahamcox.mud.events

import org.slf4j.LoggerFactory

/**
 * Event Manager that can be used to fire events and manage listeners
 */
class EventManager {
    /** The logger to use */
    private val LOG = LoggerFactory.getLogger(EventManager::class.java)

    /**
     * Representation of an event handler
     * @property filter Functor to determine if this handler can handle this event
     * @property handler The actual handler function
     */
    private class EventHandler(val filter: (Event<*>) -> Boolean,
                               val handler: (Event<*>) -> Unit)

    /** The actual set of listeners */
    private val listeners: MutableSet<EventHandler> = hashSetOf()

    /**
     * Register a new event listener for a particular source
     * @param handler The handler to register
     */
    fun registerListener(handler: (Event<*>) -> Unit) {
        registerListener({ e -> true }, handler)
    }

    /**
     * Register a new event listener for a particular source and event name
     * @param eventName The name of the event
     * @param handler The handler to register
     */
    fun registerListener(eventName: String, handler: (Event<*>) -> Unit) {
        registerListener({ e -> e.name == eventName }, handler)
    }

    /**
     * Register a new event listener with a filter to only match some events
     * @param filter The function to filter events with
     * @param handler The handler to register
     */
    fun registerListener(filter: (Event<*>) -> Boolean,
                         handler: (Event<*>) -> Unit) {
        registerListener(EventHandler(filter, handler))
    }

    /**
     * Actually add a handler to the event manager
     * @param handler The event handler
     */
    private fun registerListener(handler: EventHandler) {
        listeners.add(handler)
    }

    /**
     * Fire an event to all listeners
     * @param event The event to fire
     */
    fun fire(event: Event<*>) {
        LOG.debug("Firing event {}", event)
        listeners
                .filter { h -> h.filter(event) }
                .map { h -> h.handler }
                .forEach { h ->
                    LOG.debug("Firing event {} to handler {}", event, h)
                    h(event)
                }
    }

    /**
     * Fire an event to all listeners, including a payload
     * @param eventName The name of the event to fire
     * @param payload The payload of the event
     */
    fun fire(eventName: String, payload: Any) = fire(Event(eventName, payload))

    /**
     * Fire an event to all listeners, with no payload included
     * @param eventName The name of the event
     */
    fun fire(eventName: String) = fire(Event(eventName, null))
}
