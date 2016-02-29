package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.util.ReferenceCountUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import uk.co.grahamcox.mud.server.telnet.options.*
import uk.co.grahamcox.mud.server.telnet.spring.ConnectionScope
import uk.co.grahamcox.mud.server.telnet.ui.UI

class DiscardHandler(private val ui: UI) : ChannelInboundHandlerAdapter() {
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
class MudServerInitializer : ChannelInitializer<SocketChannel>(), BeanFactoryAware {
    /** The logger to use */
    private val LOG = LoggerFactory.getLogger(MudServerInitializer::class.java)

    private lateinit var springBeanFactory: BeanFactory

    override fun setBeanFactory(beanFactory: BeanFactory) {
        this.springBeanFactory = beanFactory
    }

    /**
     * Initialize the provided channel
     * @param channel The channel to initialize
     */
    override fun initChannel(channel: SocketChannel) {
        LOG.info("Received a new connection from {}", channel)

        val connectionScope = springBeanFactory.getBean("connectionScope", ConnectionScope::class.java)
        connectionScope.setActiveConnection(channel)

        val optionManager = springBeanFactory.getBean(OptionManager::class.java)

        val ui = UI(optionManager, channel)

        channel.pipeline().addLast(LoggingChannelHandler())
        channel.pipeline().addLast(TelnetMessageDecoder())
        channel.pipeline().addLast(TelnetMessageEncoder())
        channel.pipeline().addLast(TelnetOptionHandler(optionManager))

        channel.pipeline().addLast(DiscardHandler(ui))
        
        connectionScope.clearActiveConnection()
    }
}
