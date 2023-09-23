package de.twomartens.timetable.support.configuration

import de.twomartens.timetable.property.TimeProperties
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