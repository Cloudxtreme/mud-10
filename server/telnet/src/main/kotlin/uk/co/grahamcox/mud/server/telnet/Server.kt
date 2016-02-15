package uk.co.grahamcox.mud.server.telnet

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
class Server(val port: Int, private val initializer: ChannelInitializer<SocketChannel>) {
    /** The future representing the channel */
    private val channelFuture: ChannelFuture

    init {
        val bossGroup = NioEventLoopGroup()
        val workerGroup = NioEventLoopGroup()
        try {
            val bootstrap = ServerBootstrap()
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel::class.java)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(initializer)

            channelFuture = bootstrap.bind(port).sync()

            channelFuture.channel().closeFuture().sync()
        } finally {
            workerGroup.shutdownGracefully()
            bossGroup.shutdownGracefully()
        }
    }
}
