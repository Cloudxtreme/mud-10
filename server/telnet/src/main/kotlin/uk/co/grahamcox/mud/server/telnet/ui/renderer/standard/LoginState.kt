package uk.co.grahamcox.mud.server.telnet.ui.renderer.standard

import io.netty.channel.socket.SocketChannel
import uk.co.grahamcox.mud.users.User
import uk.co.grahamcox.mud.users.UserFinder
import kotlin.properties.Delegates

/**
 * Renderer State to handle authentication
 * @property userFinder the mechanism to load the user to log in as
 * @property channel The channel to talk to the user with
 */
@StateName("login")
class LoginState(private val userFinder: UserFinder,
                 private val channel: SocketChannel) : RendererState() {
    /** The user that we are trying to log in as */
    private var user: User? = null

    init {
        showInputPrompt()
    }

    /**
     * Handle receiving a command from the user. What we do with this depends on what field we are next expecting to
     * receive
     * @param command The command to receive
     */
    override fun handleCommand(command: String) {
        if (!command.isNullOrBlank()) {
            if (user == null) {
                // We've not yet looked up the user to log in as, so this was the username
                val foundUser = userFinder.findUserByEmail(command)
                if (foundUser == null) {
                    // That email address doesn't exist (yet)
                    channel.writeAndFlush("Unknown email address. Please register first\r\n")
                } else if (!foundUser.enabled) {
                    channel.writeAndFlush("That email address has been banned\r\n")
                } else {
                    user = foundUser
                }
            } else {
                // We've got a user but no password, so that's what this is
                if (user!!.password.equals(command)) {
                    // Correct password
                    channel.writeAndFlush("Welcome\r\n")
                } else {
                    // Wrong password
                    channel.writeAndFlush("Wrong password\r\n")
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
            "Email address: "
        } else {
            "Password: "
        }

        channel.writeAndFlush(message)
    }
}
