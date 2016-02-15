package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.util.ReferenceCountUtil
import org.slf4j.LoggerFactory

class DiscardHandler : ChannelInboundHandlerAdapter() {
    private val LOG = LoggerFactory.getLogger(DiscardHandler::class.java)

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        try {
            LOG.info("Received message {} on {}", msg, ctx)
        } finally {
            ReferenceCountUtil.release(msg)
        }
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
        channel.pipeline().addLast(DiscardHandler())
    }
}
