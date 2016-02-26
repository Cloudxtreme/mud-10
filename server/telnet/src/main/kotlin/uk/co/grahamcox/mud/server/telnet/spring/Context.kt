package uk.co.grahamcox.mud.server.telnet.spring

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer

/**
 * The Spring Context that represents the application as a whole
 */
@PropertySource("classpath:mud.properties")
@Configuration
@Import(TelnetContext::class)
open class Context {
    /**
     * Ensure that Properties are loaded correctly
     */
    @Bean
    open fun propertyPlaceholderConfigurer() = PropertySourcesPlaceholderConfigurer()

}
