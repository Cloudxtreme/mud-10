package uk.co.grahamcox.mud.server.telnet.ui.renderer.standard

import io.netty.channel.socket.SocketChannel

/**
 * The initial state that the renderer starts in when a client first connects
 * @property channel The channel to write to
 */
class InitialState(private val channel: SocketChannel) : RendererState {
    init {
        channel.writeAndFlush("Welcome")
    }
}

/**
 * Factory to create the Initial State of the renderer
 * @property channel The channel to work with
 */
class InitialStateFactory(private val channel: SocketChannel) : RendererStateFactory {
    override fun create(): RendererState  = InitialState(channel)
}