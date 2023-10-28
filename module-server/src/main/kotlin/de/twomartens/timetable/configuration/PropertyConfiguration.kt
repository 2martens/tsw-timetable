package de.twomartens.timetable.configuration

import de.twomartens.timetable.bahnApi.property.BahnApiProperties
import de.twomartens.timetable.property.RestTemplateTimeoutProperties
import de.twomartens.timetable.property.ServiceProperties
import de.twomartens.timetable.property.StatusProbeProperties
import de.twomartens.timetable.property.TimeProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.kubernetes.commons.leader.LeaderProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(RestTemplateTimeoutProperties::class, ServiceProperties::class,
        StatusProbeProperties::class, TimeProperties::class, BahnApiProperties::class,
        LeaderProperties::class)
open class PropertyConfiguration
