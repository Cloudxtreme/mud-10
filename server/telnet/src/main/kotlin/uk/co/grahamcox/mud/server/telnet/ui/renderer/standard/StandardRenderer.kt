package uk.co.grahamcox.mud.server.telnet.ui.renderer.standard

import uk.co.grahamcox.mud.server.telnet.ui.renderer.Renderer

/**
 * The standard renderer
 * @property renderers The map of renderers factories to use
 */
class StandardRenderer(private val renderers: Map<String, RendererStateFactory>) : Renderer {
    /** The state the the renderer is currently in */
    private var state: RendererState = createState("initial")

    /**
     * Create the state with the given name
     * @param name The name of the state
     * @return the state
     */
    private fun createState(name: String): RendererState {
        val factory = renderers.get(name) ?: throw IllegalArgumentException("Unable to get renderer factory for ${name}")
        return factory.create()
    }
}