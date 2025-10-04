package de.twomartens.support.property

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.boot.convert.DurationUnit
import java.time.Duration
import java.time.temporal.ChronoUnit

@ConfigurationProperties(prefix = "de.twomartens.timetable.statusprobe")
@Schema(description = "Properties, to configure this Application")
data class StatusProbeProperties @ConstructorBinding constructor(
        @DurationUnit(ChronoUnit.SECONDS) val scheduleDuration: Duration,
        val maxBlobFailureCount: Int,
        val maxFailurePercent: Int
)
