package uk.co.grahamcox.mud.server.telnet.ui.renderer.output

/**
 * Mechanism by which we can output messages to the client
 */
interface Outputter {
    /**
     * Actually output something to the client
     * @param output The details of what to output
     */
    fun output(output: Output)
}