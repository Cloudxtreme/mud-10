package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.slf4j.MDC

/**
 * Channel Handler to set the MDC on the Logger to represent the channel in question
 */
class LoggingChannelHandler : ChannelInboundHandlerAdapter() {
    override fun channelRegistered(ctx: ChannelHandlerContext) {
        MDC.put("connection", ctx.channel().toString())
        super.channelRegistered(ctx)
        MDC.remove("connection")
    }

    /**
     * Handle reading from the channel, ensuring that the MDC is set before the message is passed on
     */
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        MDC.put("connection", ctx.channel().toString())
        super.channelRead(ctx, msg)
        MDC.remove("connection")
    }
}
