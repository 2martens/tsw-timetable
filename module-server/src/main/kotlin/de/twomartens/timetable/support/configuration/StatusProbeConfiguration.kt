package de.twomartens.timetable.support.configuration

import de.twomartens.timetable.support.monitoring.statusprobe.CountBasedStatusProbe
import de.twomartens.timetable.support.monitoring.statusprobe.StatusProbe
import de.twomartens.timetable.support.monitoring.statusprobe.StatusProbeCriticality
import de.twomartens.timetable.support.monitoring.statusprobe.StatusProbeLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
open class StatusProbeConfiguration(private val clock: Clock) {
    @Bean
    open fun statusProbeLogger(): StatusProbeLogger {
        return StatusProbeLogger(clock)
    }

    @Bean
    open fun testStatusProbe(statusProbeLogger: StatusProbeLogger): StatusProbe {
        return CountBasedStatusProbe(
            1,
            clock, StatusProbeCriticality.K1, "testStatusProbe", statusProbeLogger
        )
    }
}