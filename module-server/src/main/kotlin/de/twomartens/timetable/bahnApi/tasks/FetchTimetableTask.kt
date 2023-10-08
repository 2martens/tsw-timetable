package de.twomartens.timetable.bahnApi.tasks

import de.twomartens.timetable.bahnApi.service.BahnApiService
import de.twomartens.timetable.bahnApi.service.BahnDatabaseService
import de.twomartens.timetable.model.Eva
import de.twomartens.timetable.model.Hour
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.time.LocalDate

@Async
open class FetchTimetableTask(
        private val eva: Eva,
        private val date: LocalDate,
        private val hour: Hour,
        private val bahnApiService: BahnApiService,
        private val bahnDatabaseService: BahnDatabaseService,
        private val executor: ThreadPoolTaskExecutor) : Runnable {
    override fun run() {
        log.info {
            "Fetch timetable: [eva: $eva], [date: $date], [hour: $hour]"
        }
        val timetable = bahnApiService.fetchTimetable(eva, date, hour)
        val storeTask = StoreTimetableTask(timetable, date.atTime(hour.value, 0),
                bahnDatabaseService)
        executor.execute(storeTask)
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}