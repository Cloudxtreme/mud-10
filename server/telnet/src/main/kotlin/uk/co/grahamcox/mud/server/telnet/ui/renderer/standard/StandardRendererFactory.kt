package uk.co.grahamcox.mud.server.telnet.ui.renderer.standard

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import uk.co.grahamcox.mud.server.telnet.ui.UIConfigOption
import uk.co.grahamcox.mud.server.telnet.ui.renderer.Renderer
import uk.co.grahamcox.mud.server.telnet.ui.renderer.RendererFactory

/**
 * Factory to produce the standard renderer
 * @property stateChanger The state changer to listen to
 */
class StandardRendererFactory(private val stateChanger: StateChanger) : RendererFactory, ApplicationContextAware {
    /** The Application Context to use to get the renderer states from */
    private lateinit var springApplicationContext: ApplicationContext

    /** {@inheritDoc} */
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.springApplicationContext = applicationContext
    }

    /**
     * Create the standard renderer
     * @param configOptions The configuration options for the renderer
     * @return the renderer
     */
    override fun createRenderer(configOptions: Map<Class<UIConfigOption>, UIConfigOption>): Renderer? =
            StandardRenderer(springApplicationContext, stateChanger)
}