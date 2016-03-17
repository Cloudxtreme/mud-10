package uk.co.grahamcox.mud.server.telnet.ui.renderer.standard

import uk.co.grahamcox.mud.server.telnet.ui.renderer.output.Output
import uk.co.grahamcox.mud.server.telnet.ui.renderer.output.Outputter

/**
 * The initial state that the renderer starts in when a client first connects
 * @property outputter The means to send output to the client
 * @property stateChanger The mechanism to change states
 */
@StateName("initial")
class InitialState(private val outputter : Outputter,
                   private val stateChanger: StateChanger) : RendererState() {

    /**
     * Upon entering the Initial state, we display a banner and then straight away move to the Login state
     */
    override fun enterState() {
        outputter.output(Output.StringOutput("Welcome"))
        stateChanger.changeState("login")
    }
}
