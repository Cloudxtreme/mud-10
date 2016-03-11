package uk.co.grahamcox.mud.server.telnet.ui.renderer.standard

import io.netty.channel.socket.SocketChannel
import uk.co.grahamcox.mud.server.telnet.TelnetMessage
import uk.co.grahamcox.mud.server.telnet.ui.renderer.Renderer

/**
 * The standard renderer
 * @property channel The channel to write to
 */
class StandardRenderer(private val channel: SocketChannel) : Renderer {
    init {
        channel.write("Welcome\r\n")
        channel.flush()
    }
}