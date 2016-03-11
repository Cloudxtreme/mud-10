package uk.co.grahamcox.mud.server.telnet.ui.renderer.standard

import io.netty.channel.socket.SocketChannel
import uk.co.grahamcox.mud.server.telnet.options.OptionManager
import uk.co.grahamcox.mud.server.telnet.ui.UIConfigOption
import uk.co.grahamcox.mud.server.telnet.ui.renderer.Renderer
import uk.co.grahamcox.mud.server.telnet.ui.renderer.RendererFactory

/**
 * Factory to produce the standard renderer
 * @property channel The channel to use for the renderer
 * @property optionManager The option manager to get the connection options from
 */
class StandardRendererFactory(private val channel: SocketChannel,
                              private val optionManager: OptionManager) : RendererFactory {
    /**
     * Create the standard renderer
     * @param configOptions The configuration options for the renderer
     * @return the renderer
     */
    override fun createRenderer(configOptions: Map<Class<UIConfigOption>, UIConfigOption>): Renderer? =
            StandardRenderer(channel)
}