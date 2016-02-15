package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import uk.co.grahamcox.mud.server.telnet.TelnetEncoder
import uk.co.grahamcox.mud.server.telnet.TelnetMessage

/**
 * Netty Encoder to convert a Telnet Message into the appropriate bytes
 */
class TelnetMessageEncoder : MessageToByteEncoder<TelnetMessage>() {
    /**
     * Encode the provided message
     * @param ctx The connection context.
     * @param input The input message
     * @param output The byte buffer to write to
     */
    override fun encode(ctx: ChannelHandlerContext, input: TelnetMessage, output: ByteBuf) {
        TelnetEncoder.encode(input)
                .forEach { b -> output.writeByte(b.toInt()) }
    }
}