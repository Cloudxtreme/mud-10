package uk.co.grahamcox.mud.server.telnet.ui.renderer.output

import io.netty.channel.socket.SocketChannel

/**
 * Base class for outputters to handle the actual communications
 * @property channel The channel to write to
 * @property renderer The output renderer to use
 */
class SimpleOutputter(private val channel: SocketChannel,
                      private val renderer: OutputRenderer) : Outputter {
    /**
     * Produce the correct output to send to the client
     * @param output The output to send
     */
    override fun output(output: Output) {
        val outputString = renderer.buildOutput(output)
        channel.writeAndFlush(outputString)
    }
}