package de.twomartens.support.property

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "de.twomartens.support.health")
@Schema(description = "Properties, to configure health check")
class HealthCheckProperties {
    lateinit var greeting: String
}