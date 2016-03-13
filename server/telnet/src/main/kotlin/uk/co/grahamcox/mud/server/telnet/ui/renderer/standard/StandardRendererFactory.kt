package uk.co.grahamcox.mud.server.telnet.ui.renderer.standard

import io.netty.channel.socket.SocketChannel
import uk.co.grahamcox.mud.server.telnet.options.OptionManager
import uk.co.grahamcox.mud.server.telnet.ui.UIConfigOption
import uk.co.grahamcox.mud.server.telnet.ui.renderer.Renderer
import uk.co.grahamcox.mud.server.telnet.ui.renderer.RendererFactory

/**
 * Factory to produce the standard renderer
 * @property renderers The map of renderer state factories to use
 */
class StandardRendererFactory(private val renderers: Map<String, RendererStateFactory>) : RendererFactory {
    /**
     * Create the standard renderer
     * @param configOptions The configuration options for the renderer
     * @return the renderer
     */
    override fun createRenderer(configOptions: Map<Class<UIConfigOption>, UIConfigOption>): Renderer? =
            StandardRenderer(renderers)
}