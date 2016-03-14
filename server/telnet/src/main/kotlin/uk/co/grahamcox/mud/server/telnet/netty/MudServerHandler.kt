package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.util.ReferenceCountUtil
import org.slf4j.LoggerFactory
import uk.co.grahamcox.mud.server.telnet.TelnetMessage
import uk.co.grahamcox.mud.server.telnet.ui.UI

/**
 * Handler to receive the actual input from the client
 * @property ui The User Interface that is representing the client
 */
class MudServerHandler(private val ui: UI) : ChannelInboundHandlerAdapter() {
    /** The logger to use */
    private val LOG = LoggerFactory.getLogger(MudServerHandler::class.java)

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        try {
            LOG.trace("Received message {} on {}", msg, ctx)
            when (msg) {
                is TelnetMessage.ByteMessage -> ui.receiveByte(msg.byte)
                else -> {
                    LOG.warn("Received a message that we can't handle: {}", msg)
                }
            }
        } finally {
            ReferenceCountUtil.release(msg)
        }
    }
}
