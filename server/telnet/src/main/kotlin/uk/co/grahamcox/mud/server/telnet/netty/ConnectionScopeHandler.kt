package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import uk.co.grahamcox.mud.server.telnet.spring.ConnectionScope

/**
 * Netty Handler to set up the Connection Scope to correctly use the correct connection
 * @property connectionScope The connection scope to manage
 */
class ConnectionScopeHandler(private val connectionScope: ConnectionScope) : ChannelInboundHandlerAdapter() {
    /**
     * Ensure that the correct connection is active for this thread for the processing of unregistered handlers elsewhere
     * When finished, actually unregister the connection altogether to free up the memory used by the connection scope beans
     * @param ctx The context to get the connection from
     */
    override fun channelUnregistered(ctx: ChannelHandlerContext) {
        connectionScope.setActiveConnection(ctx.channel())
        try {
            super.channelUnregistered(ctx)
        } finally {
            connectionScope.unregisterConnection(ctx.channel())
        }
    }

    /**
     * Ensure that for the duration of the channel being read the correct connection is marked as active
     * @param ctx The context to get the connection from
     * @param msg The message that was read
     */
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        connectionScope.setActiveConnection(ctx.channel())
        try {
            super.channelRead(ctx, msg)
        } finally {
            connectionScope.clearActiveConnection()
        }
    }
}
