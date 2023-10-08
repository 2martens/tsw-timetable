package de.twomartens.timetable.bahnApi.tasks

import de.twomartens.timetable.bahnApi.model.dto.BahnTimetable
import de.twomartens.timetable.bahnApi.service.BahnDatabaseService
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Async
import java.time.LocalDateTime

@Async
open class StoreTimetableTask(
        private val timetable: BahnTimetable,
        private val dateTime: LocalDateTime,
        private val bahnDatabaseService: BahnDatabaseService
) : Runnable {
    override fun run() {
        log.info { "Store timetable: [eva: ${timetable.eva}], station: ${timetable.station}]" }
        bahnDatabaseService.storeTimetable(timetable, dateTime)
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}