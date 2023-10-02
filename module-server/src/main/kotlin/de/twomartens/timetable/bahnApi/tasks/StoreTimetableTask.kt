package de.twomartens.timetable.bahnApi.tasks

import de.twomartens.timetable.bahnApi.model.dto.BahnTimetable
import de.twomartens.timetable.bahnApi.service.BahnDatabaseService
import org.springframework.scheduling.annotation.Async
import java.time.LocalDateTime

@Async
open class StoreTimetableTask(
        private val timetable: BahnTimetable,
        private val dateTime: LocalDateTime,
        private val bahnDatabaseService: BahnDatabaseService
) : Runnable {
    override fun run() {
        bahnDatabaseService.storeTimetable(timetable, dateTime)
    }
}