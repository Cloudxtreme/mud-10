package uk.co.grahamcox.mud.server.telnet.netty

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

/**
 * The actual Telnet Server.
 * Note that this server will be actively listening on construction.
 * @property port The port to listen on
 * @property initializer The initializer to use for the client channels
 */
class Server(private val port: Int, private val initializer: ChannelInitializer<SocketChannel>) {
    /** The main event loop group */
    private val bossGroup: NioEventLoopGroup

    /** The worker event loop group */
    private val workerGroup: NioEventLoopGroup

    /** The channel future for the server */
    private val channelFuture: ChannelFuture

    init {
        bossGroup = NioEventLoopGroup()
        workerGroup = NioEventLoopGroup()

        val bootstrap = ServerBootstrap()
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(initializer)

        channelFuture = bootstrap.bind(port).sync()
    }

    /**
     * Shutdown the server
     */
    fun shutdown() {
        workerGroup.shutdownGracefully()
        bossGroup.shutdownGracefully()
    }
}
