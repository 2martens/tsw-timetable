package de.twomartens.timetable.bahnApi.service

import de.twomartens.timetable.bahnApi.model.Eva
import de.twomartens.timetable.bahnApi.model.FetchDates
import de.twomartens.timetable.bahnApi.model.TaskFactory
import de.twomartens.timetable.bahnApi.model.db.ScheduledFetchTask
import de.twomartens.timetable.bahnApi.repository.ScheduledFetchTaskRepository
import de.twomartens.timetable.model.db.TswRoute
import de.twomartens.timetable.types.Hour
import de.twomartens.timetable.types.HourAtDay
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ScheduledTaskService(
        private val scheduledFetchTaskRepository: ScheduledFetchTaskRepository,
        private val taskFactory: TaskFactory,
        private val taskScheduler: TaskScheduler
) {
    fun initializeScheduledTasks() {
        log.info { "Initialize persisted scheduled fetch tasks" }
        val scheduledTasks = scheduledFetchTaskRepository.findAll()
        taskFactory.initializeWithExistingTasks(scheduledTasks)
        taskScheduler.scheduleFetchTasks(scheduledTasks)
    }

    fun scheduleTimetableFetch(tswRoute: TswRoute, fetchedDate: LocalDate) {
        log.info {
            "Schedule timetable fetch: [route ${tswRoute.name}]"
        }
        val fetchDates = calculateDatesToFetch(fetchedDate)
        val newTasks = buildScheduledTasks(tswRoute, fetchDates)

        storeTasksInDatabase(newTasks)
        taskScheduler.scheduleFetchTasks(newTasks)
    }

    private fun calculateDatesToFetch(fetchedDate: LocalDate): FetchDates {
        val previousDay = fetchedDate.minusDays(1)
        val nextDay = fetchedDate.plusDays(1)
        return FetchDates(previousDay, fetchedDate, nextDay)
    }

    private fun buildScheduledTasks(
            tswRoute: TswRoute,
            fetchDates: FetchDates
    ): List<ScheduledFetchTask> {
        val newTasks = mutableListOf<ScheduledFetchTask>()
        tswRoute.stationIds.forEach {
            val stationId = it
            val eva = Eva.of(stationId)
            var hourAtDay = HourAtDay.of(Hour.of(23), fetchDates.previousDay)
            var newTask = taskFactory.createTaskAndUpdateCounter(eva, hourAtDay)
            newTasks.add(newTask)
            for (hour in 0..23) {
                hourAtDay = HourAtDay.of(Hour.of(hour), fetchDates.fetchDate)
                newTask = taskFactory.createTaskAndUpdateCounter(eva, hourAtDay)
                newTasks.add(newTask)
            }
            for (hour in 0..3) {
                hourAtDay = HourAtDay.of(Hour.of(hour), fetchDates.nextDate)
                newTask = taskFactory.createTaskAndUpdateCounter(eva, hourAtDay)
                newTasks.add(newTask)
            }
        }
        return newTasks
    }

    private fun storeTasksInDatabase(newTasks: List<ScheduledFetchTask>) {
        scheduledFetchTaskRepository.saveAll(newTasks)
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}