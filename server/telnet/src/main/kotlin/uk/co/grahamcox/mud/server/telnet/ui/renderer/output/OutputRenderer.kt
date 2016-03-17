package uk.co.grahamcox.mud.server.telnet.ui.renderer.output

/**
 * Interface describing how to render an Output object as a String
 */
interface OutputRenderer {
    /**
     * Actually build the output string to send
     * @param output The output object to process
     * @return the string representing this output object
     */
    fun buildOutput(output: Output) : String

}