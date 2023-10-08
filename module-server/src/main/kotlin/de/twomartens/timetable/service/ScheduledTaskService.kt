package de.twomartens.timetable.service

import de.twomartens.timetable.bahnApi.model.db.ScheduledFetchTask
import de.twomartens.timetable.bahnApi.repository.ScheduledFetchTaskRepository
import de.twomartens.timetable.bahnApi.service.BahnApiService
import de.twomartens.timetable.bahnApi.service.BahnDatabaseService
import de.twomartens.timetable.bahnApi.tasks.DeleteScheduledTask
import de.twomartens.timetable.bahnApi.tasks.FetchTimetableTask
import de.twomartens.timetable.model.Hour
import de.twomartens.timetable.model.Tasks
import de.twomartens.timetable.model.db.TswRoute
import de.twomartens.timetable.property.TimeProperties
import mu.KotlinLogging
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ScheduledTaskService(
        private val timeProperties: TimeProperties,
        private val scheduledFetchTaskRepository: ScheduledFetchTaskRepository,
        private val threadPoolTaskScheduler: ThreadPoolTaskScheduler,
        private val bahnApiService: BahnApiService,
        private val bahnDatabaseService: BahnDatabaseService,
        private val threadPoolTaskExecutor: ThreadPoolTaskExecutor
) {
    private val tasks: Tasks = Tasks()

    fun initializeScheduledTasks() {
        log.info { "Initialize persisted scheduled fetch tasks" }
        val scheduledTasks = scheduledFetchTaskRepository.findAll()
        tasks.tasks = scheduledTasks
        scheduleTasks(scheduledTasks)
    }

    fun scheduleTimetableFetch(tswRoute: TswRoute, fetchedDate: LocalDate) {
        log.info {
            "Schedule timetable fetch: [route ${tswRoute.name}]"
        }
        val previousDay = fetchedDate.minusDays(1)
        val nextDay = fetchedDate.plusDays(1)
        val newTasks = mutableListOf<ScheduledFetchTask>()
        tswRoute.stations.forEach {
            val eva = it
            var newTask = tasks.createAndAddTask(eva, date = previousDay, hour = Hour(23))
            newTasks.add(newTask)
            for (hour in 0..23) {
                newTask = tasks.createAndAddTask(eva, date = fetchedDate, hour = Hour(hour))
                newTasks.add(newTask)
            }
            for (hour in 0..3) {
                newTask = tasks.createAndAddTask(eva, date = nextDay, hour = Hour(hour))
                newTasks.add(newTask)
            }
        }

        scheduledFetchTaskRepository.saveAll(newTasks)
        scheduleTasks(newTasks)
    }

    private fun scheduleTasks(scheduledTasks: List<ScheduledFetchTask>) {
        scheduledTasks.forEach {
            val zonedExecutionTime = it.scheduledExecutionDateTime
                    .atZone(timeProperties.defaultTimeZone)
            val deleteTask = DeleteScheduledTask(scheduledFetchTaskRepository, it)
            val timetableTask = FetchTimetableTask(it.eva, it.fetchedDate, it.fetchedTime,
                    bahnApiService, bahnDatabaseService, threadPoolTaskExecutor)
            threadPoolTaskScheduler.schedule(timetableTask, zonedExecutionTime.toInstant())
            threadPoolTaskScheduler.schedule(deleteTask, zonedExecutionTime.toInstant())
        }
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}