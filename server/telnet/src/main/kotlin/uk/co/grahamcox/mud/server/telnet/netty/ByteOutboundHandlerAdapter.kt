package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder
import org.slf4j.LoggerFactory
import uk.co.grahamcox.mud.server.telnet.TelnetMessage

/**
 * Encoder that will convert bare bytes into Telnet Messages so that they can be encoded later
 */
class ByteOutboundHandlerAdapter : MessageToMessageEncoder<Byte>() {
    /** The logger to use */
    private val LOG = LoggerFactory.getLogger(ByteOutboundHandlerAdapter::class.java)

    /**
     * Encode the byte into a ByteMessage
     * @param ctx The context to work with
     * @param msg The message to encode
     * @param out The output list to write to
     */
    override fun encode(ctx: ChannelHandlerContext,
                        msg: Byte,
                        out: MutableList<Any>) {
        LOG.trace("Encoding byte {} as a Telnet Message", msg)

        out.add(TelnetMessage.ByteMessage(msg))
    }
}