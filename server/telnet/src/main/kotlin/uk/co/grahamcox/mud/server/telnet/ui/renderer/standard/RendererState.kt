package uk.co.grahamcox.mud.server.telnet.ui.renderer.standard

/**
 * Interface describing the current state that the renderer is in.
 * Note that the renderer can only ever be in a single state.
 */
abstract class  RendererState {
    /**
     * Handle the fact that we have just entered the state
     * This needs to be separated from the constructor for call ordering purposes
     */
    open fun enterState() {

    }

    /**
     * Handle a command received from the client
     * @param command The command to handle
     */
    open fun handleCommand(command: String) {

    }
}

/**
 * Annotation that must be applied to the state class to indicate the name of the state
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class StateName(val value: String)
