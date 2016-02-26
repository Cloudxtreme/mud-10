package uk.co.grahamcox.mud.server.telnet.spring

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import uk.co.grahamcox.mud.server.telnet.netty.MudServerInitializer
import uk.co.grahamcox.mud.server.telnet.netty.Server

/**
 * The configuration for the Telnet Server
 */
@Configuration
open class TelnetContext {

    /**
     * The Netty Channel Initializer for the MUD
     */
    @Bean
    open fun mudServerInitializer() = MudServerInitializer()

    /**
     * Create the Telnet Server to use
     */
    @Autowired
    @Bean(destroyMethod = "shutdown")
    open fun telnetServer(@Value("\${portNumber}") port: Int,
                          initializer: ChannelInitializer<SocketChannel>) = Server(port, initializer)
}
