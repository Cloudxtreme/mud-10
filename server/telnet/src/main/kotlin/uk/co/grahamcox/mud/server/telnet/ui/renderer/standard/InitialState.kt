package uk.co.grahamcox.mud.server.telnet.ui.renderer.standard

import io.netty.channel.socket.SocketChannel

/**
 * The initial state that the renderer starts in when a client first connects
 * @property channel The channel to write to
 * @property stateChanger The mechanism to change states
 */
@StateName("initial")
class InitialState(private val channel: SocketChannel,
                   private val stateChanger: StateChanger) : RendererState() {

    /**
     * Upon entering the Initial state, we display a banner and then straight away move to the Login state
     */
    override fun enterState() {
        channel.writeAndFlush("Welcome\r\n")
        stateChanger.changeState("login")
    }
}
