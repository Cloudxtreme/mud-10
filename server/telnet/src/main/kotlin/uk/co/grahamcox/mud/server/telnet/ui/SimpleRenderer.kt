package uk.co.grahamcox.mud.server.telnet.ui

import io.netty.channel.socket.SocketChannel
import uk.co.grahamcox.mud.server.telnet.TelnetMessage

/**
 * Implementation of a Renderer that works with any system no matter what
 */
class SimpleRenderer(private val channel: SocketChannel) : Renderer {
    /**
     * Render the current state of the game to the screen
     */
    override fun render() {
        "Hello, World\r\n".toByteArray()
                .map { b -> TelnetMessage.ByteMessage(b) }
                .forEach { message -> channel.write(message) }
        channel.flush()
    }
}
