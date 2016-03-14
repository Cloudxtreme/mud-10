package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder
import org.slf4j.LoggerFactory

/**
 * Handler Adapter that will convert any String that comes through into a series of bytes
 */
class StringOutboundHandlerAdapter : MessageToMessageEncoder<CharSequence>() {
    /** The logger to use */
    private val LOG = LoggerFactory.getLogger(StringOutboundHandlerAdapter::class.java)

    /**
     * Encode the given character sequence into a simple list of bytes
     * @param ctx The context to work with
     * @param str The string to encode
     * @param out The output list to write to
     */
    override fun encode(ctx: ChannelHandlerContext,
                        str: CharSequence,
                        out: MutableList<Any>) {
        LOG.trace("Encoding string '{}' as bytes", str)
        out.add(str.toString().toByteArray())
    }
}