package uk.co.grahamcox.mud.server.telnet.ui.renderer.output

/**
 * Simple implementation of the Output Renderer
 */
class SimpleRenderer : OutputRenderer {

    /**
     * Build the actual string to send for the given Output object
     * @param output The output object
     * @return the string
     */
    override fun buildOutput(output: Output): String =
        when (output) {
            is Output.CompositeOutput ->
                    output.output
                            .map { o -> buildOutput(o) }
                            .joinToString("", "", "")
            is Output.StringOutput ->
                    if (output.newline) {
                        output.output + "\r\n"
                    } else {
                        output.output
                    }
        }
}