package uk.co.grahamcox.mud.server.telnet.ui.renderer.standard

import org.slf4j.LoggerFactory
import uk.co.grahamcox.mud.events.EventManager

/**
 * Mechanism that we can use to indicate that we want to change states
 */
class StateChanger {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(StateChanger::class.java)

        /** The event to indicate that we want to change state */
        val STATE_CHANGE_EVENT = StateChanger::class.qualifiedName + "StateChange"
    }

    private val eventManager = EventManager()

    /**
     * Request that we change state into the provided state
     * @param newState The state that we want to change into
     */
    fun changeState(newState: String) {
        LOG.debug("Requested to change into the {} state", newState)

        eventManager.fire(STATE_CHANGE_EVENT, newState)
    }

    /**
     * Register a listener to be told when we change states
     * @param listener The listener to register
     */
    fun registerStateChangeListener(listener: (newState: String) -> Unit) {
        eventManager.registerListener(STATE_CHANGE_EVENT) { event ->
            listener(event.payload as String)
        }
    }
}