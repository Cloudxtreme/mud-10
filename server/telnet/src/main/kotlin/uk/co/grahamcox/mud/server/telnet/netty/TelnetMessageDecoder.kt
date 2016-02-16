package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.util.AttributeKey
import uk.co.grahamcox.mud.server.telnet.TelnetDecoder

/**
 * Netty Decoder to convert an incoming stream of bytes into Telnet Messages
 */
class TelnetMessageDecoder : ByteToMessageDecoder() {
    /** The attribute key for the telnet decoder */
    private val decoderAttributeKey = AttributeKey.valueOf<TelnetDecoder>(TelnetDecoder::class.qualifiedName)

    /**
     * Actually decode the bytes form the incoming Byte Buffer
     * @param ctx The connection context
     * @param input The input Byte Buffer
     * @param output The list to add any decoded telnet messages to
     */
    override fun decode(ctx: ChannelHandlerContext, input: ByteBuf, output: MutableList<Any>) {
        val decoderAttribute = ctx.attr(decoderAttributeKey)
        val decoder = decoderAttribute.getOrSet { TelnetDecoder() }

        while (input.isReadable) {
            val message = decoder.inject(input.readByte())
            if (message != null) {
                output.add(message)
            }
        }
    }
}
