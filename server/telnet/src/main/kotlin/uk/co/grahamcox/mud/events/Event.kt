package uk.co.grahamcox.mud.events

/**
 * Event that can be fired
 * @property name The name of the event
 * @property payload The payload of the event
 */
data class Event<T>(val name: String, val payload: T? = null)