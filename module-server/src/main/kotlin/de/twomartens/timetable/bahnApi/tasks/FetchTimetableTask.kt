package de.twomartens.timetable.bahnApi.tasks

import de.twomartens.timetable.bahnApi.service.BahnApiService
import de.twomartens.timetable.bahnApi.service.BahnDatabaseService
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.time.LocalDate
import java.time.LocalTime

@Async
open class FetchTimetableTask(
        private val eva: Int,
        private val date: LocalDate,
        private val hour: LocalTime,
        private val bahnApiService: BahnApiService,
        private val bahnDatabaseService: BahnDatabaseService,
        private val executor: ThreadPoolTaskExecutor) : Runnable {
    override fun run() {
        val timetable = bahnApiService.fetchTimetable(eva, date, hour)
        val storeTask = StoreTimetableTask(timetable, date.atTime(hour), bahnDatabaseService)
        executor.execute(storeTask)
    }
}