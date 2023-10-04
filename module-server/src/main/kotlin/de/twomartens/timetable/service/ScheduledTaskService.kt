package de.twomartens.timetable.service

import de.twomartens.timetable.bahnApi.repository.ScheduledFetchTaskRepository
import de.twomartens.timetable.bahnApi.service.BahnApiService
import de.twomartens.timetable.bahnApi.service.BahnDatabaseService
import de.twomartens.timetable.bahnApi.tasks.DeleteScheduledTask
import de.twomartens.timetable.bahnApi.tasks.FetchTimetableTask
import de.twomartens.timetable.property.TimeProperties
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Service

@Service
class ScheduledTaskService(
        private val timeProperties: TimeProperties,
        private val scheduledFetchTaskRepository: ScheduledFetchTaskRepository,
        private val threadPoolTaskScheduler: ThreadPoolTaskScheduler,
        private val bahnApiService: BahnApiService,
        private val bahnDatabaseService: BahnDatabaseService,
        private val threadPoolTaskExecutor: ThreadPoolTaskExecutor
) {
    fun initializeScheduledTasks() {
        val scheduledTasks = scheduledFetchTaskRepository.findAll()
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
}