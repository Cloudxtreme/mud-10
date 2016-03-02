package uk.co.grahamcox.mud.server.telnet.ui

import io.netty.channel.socket.SocketChannel
import uk.co.grahamcox.mud.server.telnet.TelnetMessage
import uk.co.grahamcox.mud.server.telnet.options.NAWSOption

/**
 * Factory to create the Full ANSI Renderer
 */
class FullAnsiRendererFactory : RendererFactory {
    /**
     * Create the renderer
     * @param channel The channel to render to
     * @param config The config to create the renderer with
     * @return the renderer
     */
    override fun createRenderer(channel: SocketChannel, config: RendererConfig): Renderer {
        return FullAnsiRenderer(channel, config.windowSize ?: throw IllegalStateException("Window size is mandatory"))
    }

    /**
     * Check if we can create the renderer with this config.
     * This is possible if the config includes a Window Size, and if the Terminal Type is known to support ANSI
     * @param config The config
     * @return true if the config is suitable for this renderer. False if not
     */
    override fun canCreateRenderer(config: RendererConfig): Boolean {
        return config.windowSize != null
    }
}

/**
 * The actual renderer to use for a Full ANSI Graphics display
 * @param channel The channel to render to
 * @property windowSize The size of the window to support
 */
class FullAnsiRenderer(private val channel: SocketChannel,
                       private val windowSize: NAWSOption.WindowSizePayload) : Renderer {
    /**
     * Render the UI
     */
    override fun render() {
        ANSI.clearScreen().map { TelnetMessage.ByteMessage(it) }
            .forEach { channel.write(it) }
        ANSI.moveTo(0, 0).map { TelnetMessage.ByteMessage(it) }
                .forEach { channel.write(it) }

        buildBoxLine(windowSize.width, '╔', '═', '╗').map { TelnetMessage.ByteMessage(it) }
                .forEach { channel.write(it) }

        val middleLines = buildBoxLine(windowSize.width, '║', ' ', '║');
        for (i in 0..10) {
            middleLines.map { TelnetMessage.ByteMessage(it) }
                    .forEach { channel.write(it) }
        }
        buildBoxLine(windowSize.width, '╚', '═', '╝').map { TelnetMessage.ByteMessage(it) }
                .forEach { channel.write(it) }
        channel.flush()
    }

    private fun buildBoxLine(width: Int, left: Char, middle: Char, right: Char): ByteArray {
        val boxLine = StringBuilder()
        boxLine.append(left)

        for (i in 0..(width - 3)) {
            boxLine.append(middle);
        }
        boxLine.append(right);

        return boxLine.toString().toByteArray()
    }
}
