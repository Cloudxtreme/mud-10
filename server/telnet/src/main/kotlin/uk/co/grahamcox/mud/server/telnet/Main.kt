package uk.co.grahamcox.mud.server.telnet

import org.springframework.context.support.ClassPathXmlApplicationContext
import uk.co.grahamcox.mud.server.telnet.netty.Server

/**
 * Main entry point, for now
 */
fun main(args: Array<String>) {
    val applicationContext = ClassPathXmlApplicationContext(
            "classpath:uk/co/grahamcox/mud/spring/context.xml",
            "classpath:uk/co/grahamcox/mud/server/telnet/spring/context.xml"
    )

    applicationContext.getBean(Server::class.java)
}
