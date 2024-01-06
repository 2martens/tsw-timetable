package de.twomartens.support.configuration

import de.twomartens.support.property.TimeProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
open class ClockConfiguration(private val properties: TimeProperties) {

    @Bean
    open fun clock(): Clock {
        return Clock.system(properties.defaultTimeZone)
    }
}