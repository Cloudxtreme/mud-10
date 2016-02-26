package uk.co.grahamcox.mud.server.telnet

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import uk.co.grahamcox.mud.server.telnet.netty.Server
import uk.co.grahamcox.mud.server.telnet.spring.Context

/**
 * Main entry point, for now
 */
fun main(args: Array<String>) {
    val applicationContext = AnnotationConfigApplicationContext(Context::class.java)

    applicationContext.getBean(Server::class.java)
}
