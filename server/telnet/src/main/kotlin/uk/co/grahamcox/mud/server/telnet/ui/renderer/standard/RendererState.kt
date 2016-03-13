package uk.co.grahamcox.mud.server.telnet.ui.renderer.standard

/**
 * Interface describing the current state that the renderer is in.
 * Note that the renderer can only ever be in a single state.
 */
interface RendererState {

}

/**
 * Factory to use to generate a new state for the renderer
 */
interface RendererStateFactory {
    /**
     * Create a new instance of the appropriate state
     * @return the new instance of the state
     */
    fun create() : RendererState
}