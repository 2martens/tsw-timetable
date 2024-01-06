package de.twomartens.timetable.configuration

import de.twomartens.support.property.HealthCheckProperties
import de.twomartens.support.property.RestTemplateTimeoutProperties
import de.twomartens.support.property.StatusProbeProperties
import de.twomartens.support.property.TimeProperties
import de.twomartens.timetable.bahnApi.property.BahnApiProperties
import de.twomartens.timetable.property.ServiceProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.kubernetes.commons.leader.LeaderProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(RestTemplateTimeoutProperties::class, ServiceProperties::class,
        StatusProbeProperties::class, TimeProperties::class, BahnApiProperties::class,
        HealthCheckProperties::class,
        LeaderProperties::class)
open class PropertyConfiguration
