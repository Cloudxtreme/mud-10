package uk.co.grahamcox.mud.server.telnet.ui

import io.netty.channel.socket.SocketChannel
import uk.co.grahamcox.mud.server.telnet.options.NAWSOption

/**
 * Interface describing a renderer to draw the output to the screen
 */
interface Renderer {
    /**
     * Actually render the output to the screen
     */
    fun render();
}

/**
 * Configuration for a Renderer
 * @param windowSize The size of the window
 * @param terminalType The type of terminal
 */
data class RendererConfig(val windowSize: NAWSOption.WindowSizePayload?,
                          val terminalType: String?)

/**
 * Factory to use to create the renderer to use
 */
interface RendererFactory {
    /**
     * Actually create a renderer using the given configuration
     * @param channel The channel to render to
     * @param config The config to check
     * @return the renderer
     */
    fun createRenderer(channel: SocketChannel, config: RendererConfig): Renderer

    /**
     * Determine if this factory can create a renderer using the given config
     * @param config The config to check
     * @return True if we can support this config. False if not
     */
    fun canCreateRenderer(config: RendererConfig): Boolean
}
