package uk.co.grahamcox.mud.server.telnet.ui.renderer.standard

import io.netty.channel.socket.SocketChannel

/**
 * Renderer State to handle authentication
 */
@StateName("login")
class LoginState(private val channel: SocketChannel) : RendererState {
    init {
        channel.writeAndFlush("Username: ");
    }
}