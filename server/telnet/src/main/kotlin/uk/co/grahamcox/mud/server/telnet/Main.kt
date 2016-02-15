package uk.co.grahamcox.mud.server.telnet

/**
 * Main entry point, for now
 */
fun main(args: Array<String>) {
    val initializer = MudServerInitializer()
    val server = Server(12345, initializer)
}
