package uk.co.grahamcox.mud.server.telnet.ui.renderer

import org.slf4j.LoggerFactory
import uk.co.grahamcox.mud.server.telnet.ui.UIConfigOption

/**
 * Interface describing a means to create a Renderer
 */
interface RendererFactory {
    /**
     * Attempt to create a renderer for the given configuration options
     * @param configOptions The configuration options that we have determined to use
     * @return the renderer to use. If this factory can't produce a renderer then return null instead
     */
    fun createRenderer(configOptions: Map<Class<UIConfigOption>, UIConfigOption>): Renderer?
}

/**
 * Composite implementation of the Renderer Factory that will work through a list of delegates until one succeeds
 * @property delegates the list of delegates to work with
 */
class CompositeRendererFactory(private val delegates: List<RendererFactory>) : RendererFactory {
    /** The logger to use */
    private val LOG = LoggerFactory.getLogger(CompositeRendererFactory::class.java)

    /**
     * Work through the list of Renderer Factories that we have until one succeeds
     */
    override fun createRenderer(configOptions: Map<Class<UIConfigOption>, UIConfigOption>): Renderer? =
        delegates.asSequence()
            .map { factory ->
                LOG.debug("Attempting to create a renderer with factory {} and options {}", factory, configOptions)
                val renderer = factory.createRenderer(configOptions)
                LOG.debug("Result of creating renderer with factory {} and options {}: {}", factory, configOptions, renderer)
                renderer
            }
            .filterNotNull()
            .firstOrNull()
}
