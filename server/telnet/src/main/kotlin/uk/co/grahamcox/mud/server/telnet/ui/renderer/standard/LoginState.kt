package uk.co.grahamcox.mud.server.telnet.ui.renderer.standard

import uk.co.grahamcox.mud.server.telnet.ui.renderer.output.Output
import uk.co.grahamcox.mud.server.telnet.ui.renderer.output.Outputter
import uk.co.grahamcox.mud.users.User
import uk.co.grahamcox.mud.users.UserFinder

/**
 * Renderer State to handle authentication
 * @property userFinder the mechanism to load the user to log in as
 * @property outputter The mechanism to send messages to the user
 */
@StateName("login")
class LoginState(private val userFinder: UserFinder,
                 private val outputter: Outputter) : RendererState() {
    /** The user that we are trying to log in as */
    private var user: User? = null

    init {
        showInputPrompt()
    }

    /**
     * Handle receiving a command from the user. What we do with this depends on what field we are next expecting to
     * receive
     * @param input The input to receive
     */
    override fun handleCommand(input: Input) {
        when (input) {
            is Input.LineInput -> handleInputCommand(input.line)
        }
    }

    /**
     * Handle a command that came in as input
     * @param command The command to handle
     */
    private fun handleInputCommand(command: String) {
        if (!command.isNullOrBlank()) {
            if (user == null) {
                // We've not yet looked up the user to log in as, so this was the username
                val foundUser = userFinder.findUserByEmail(command)
                if (foundUser == null) {
                    // That email address doesn't exist (yet)
                    outputter.output(Output.StringOutput("Unknown email address. Please register first"))
                } else if (!foundUser.enabled) {
                    outputter.output(Output.StringOutput("That email address has been banned"))
                } else {
                    user = foundUser
                }
            } else {
                // We've got a user but no password, so that's what this is
                if (user!!.password.equals(command)) {
                    // Correct password
                    outputter.output(Output.StringOutput("Welcome"))
                } else {
                    // Wrong password
                    outputter.output(Output.StringOutput("Wrong password"))
                }
            }
        }
        showInputPrompt()
    }

    /**
     * Show the input prompt for the state that we are currently in
     */
    private fun showInputPrompt() {
        val message = if (user == null) {
            Output.StringOutput("Email address: ", false)
        } else {
            Output.StringOutput("Password: ", false)
        }

        outputter.output(message)
    }
}
