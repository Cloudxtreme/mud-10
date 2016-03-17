package uk.co.grahamcox.mud.server.telnet.ui.renderer.output

/**
 * Base class for all possible outputs
 */
sealed class Output {
    /**
     * Output object that is actually just a collection of other outputs
     * @property output The outputs to process
     */
    class CompositeOutput(val output: List<Output>) : Output()

    /**
     * Output object that is a String, either with or without a newline at the end
     * @property output The string to output
     * @property newline True if a newline should be placed at the end
     */
    class StringOutput(val output: String, val newline: Boolean = true) : Output()
}