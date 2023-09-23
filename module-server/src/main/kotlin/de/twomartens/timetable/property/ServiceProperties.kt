package de.twomartens.timetable.property

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.context.config.annotation.RefreshScope

@RefreshScope
@ConfigurationProperties(prefix = "de.twomartens.timetable")
@Schema(description = "Properties, to configure this Application")
class ServiceProperties {
    lateinit var greeting: String
}
