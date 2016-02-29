package uk.co.grahamcox.mud.server.telnet.spring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.CustomScopeConfigurer
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

    /**
     * The scope to use for Connection scoped objects
     * @return the connection scope
     */
    @Bean
    open fun connectionScope() = ConnectionScope()

    /**
     * Configure the custom scopes to use
     * @return the custom scope configurer
     */
    @Autowired
    @Bean
    open fun customScopes(connectionScope: ConnectionScope): CustomScopeConfigurer {
        val configurer = CustomScopeConfigurer()
        configurer.addScope("connection", connectionScope)
        return configurer
    }
}
