package de.twomartens.timetable.bahnApi.tasks

import de.twomartens.timetable.bahnApi.model.Eva
import de.twomartens.timetable.bahnApi.service.BahnApiService
import de.twomartens.timetable.bahnApi.service.TaskScheduler
import de.twomartens.timetable.types.HourAtDay
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Async

@Async
open class FetchTimetableTask(
        private val eva: Eva,
        private val hourAtDay: HourAtDay,
        private val bahnApiService: BahnApiService,
        private val scheduler: TaskScheduler) : Runnable {
    override fun run() {
        log.info {
            "Fetch timetable: [eva: $eva], [date: ${hourAtDay.date}], [hour: ${hourAtDay.hour}]"
        }
        val timetable = bahnApiService.fetchTimetable(eva, hourAtDay)
        scheduler.scheduleStoreTask(timetable, hourAtDay)
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}