package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder
import org.slf4j.LoggerFactory

/**
 * Handler Adapter to convert a byte array into a stream of bytes
 */
class ByteArrayOutboundHandlerAdapter : MessageToMessageEncoder<ByteArray>() {
    /** The logger to use */
    private val LOG = LoggerFactory.getLogger(ByteArrayOutboundHandlerAdapter::class.java)

    /**
     * Encode the byte array into a stream of bytes
     * @param ctx The context to work with
     * @param msg The message to encode
     * @param out The output list to write to
     */
    override fun encode(ctx: ChannelHandlerContext,
                        msg: ByteArray,
                        out: MutableList<Any>) {
        LOG.trace("Encoding byte array {} as bytes", msg)
        out.addAll(msg.toList())
    }
}