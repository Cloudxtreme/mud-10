package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.util.ReferenceCountUtil
import org.slf4j.LoggerFactory
import uk.co.grahamcox.mud.server.telnet.TelnetMessage
import java.nio.charset.Charset

class DiscardHandler : ChannelInboundHandlerAdapter() {
    private val LOG = LoggerFactory.getLogger(DiscardHandler::class.java)

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        try {
            LOG.info("Received message {} on {}", msg, ctx)
        } finally {
            ReferenceCountUtil.release(msg)
        }
    }

    override fun channelRegistered(ctx: ChannelHandlerContext) {
        ctx.write(TelnetMessage.NegotiationMessage(TelnetMessage.NegotiationMessage.Negotiation.DO, 24))
        ctx.flush()

        ctx.write(TelnetMessage.SubnegotiationMessage(24, listOf(1)))
        ctx.flush()

        "£☃𐄡\r\n".toByteArray(Charset.forName("UTF-8"))
                .map { b -> TelnetMessage.ByteMessage(b) }
                .forEach { m -> ctx.write(m) }
        ctx.flush()

    }
}

/**
 * Channel Initializer for the MUD
 */
class MudServerInitializer : ChannelInitializer<SocketChannel>() {
    /**
     * Initialize the provided channel
     * @param channel The channel to initialize<
     */
    override fun initChannel(channel: SocketChannel) {
        channel.pipeline().addLast(TelnetMessageDecoder())
        channel.pipeline().addLast(TelnetMessageEncoder())
        channel.pipeline().addLast(DiscardHandler())
    }
}
