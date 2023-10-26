package de.twomartens.timetable.bahnApi.service

import de.twomartens.timetable.bahnApi.model.db.ScheduledFetchTask
import de.twomartens.timetable.bahnApi.model.dto.BahnTimetable
import de.twomartens.timetable.bahnApi.repository.ScheduledFetchTaskRepository
import de.twomartens.timetable.bahnApi.tasks.DeleteScheduledTask
import de.twomartens.timetable.bahnApi.tasks.FetchTimetableTask
import de.twomartens.timetable.bahnApi.tasks.StoreTimetableTask
import de.twomartens.timetable.property.TimeProperties
import de.twomartens.timetable.types.HourAtDay
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class TaskScheduler(
        private val timeProperties: TimeProperties,
        private val threadPoolTaskScheduler: ThreadPoolTaskScheduler,
        private val threadPoolTaskExecutor: ThreadPoolTaskExecutor,
        private val bahnApiService: BahnApiService,
        private val bahnDatabaseService: BahnDatabaseService,
        private val scheduledFetchTaskRepository: ScheduledFetchTaskRepository
) {
    fun scheduleFetchTasks(scheduledTasks: List<ScheduledFetchTask>) {
        scheduledTasks.forEach {
            val zonedExecutionTime = it.scheduledExecutionDateTime
                    .atZone(timeProperties.defaultTimeZone)
            scheduleDeleteTask(it, zonedExecutionTime)
            scheduleFetchTimetableTask(it, zonedExecutionTime)
        }
    }

    fun scheduleStoreTask(timetable: BahnTimetable, hourAtDay: HourAtDay) {
        val storeTask = StoreTimetableTask(timetable, hourAtDay, bahnDatabaseService)
        threadPoolTaskExecutor.execute(storeTask)
    }

    private fun scheduleDeleteTask(
            scheduledFetchTask: ScheduledFetchTask,
            zonedExecutionTime: ZonedDateTime
    ) {
        val deleteTask = DeleteScheduledTask(scheduledFetchTaskRepository, scheduledFetchTask)
        threadPoolTaskScheduler.schedule(deleteTask, zonedExecutionTime.toInstant())
    }

    private fun scheduleFetchTimetableTask(
            scheduledFetchTask: ScheduledFetchTask,
            zonedExecutionTime: ZonedDateTime
    ) {
        val timetableTask = FetchTimetableTask(scheduledFetchTask.eva,
                scheduledFetchTask.fetchedDateTime,
                bahnApiService, this)
        threadPoolTaskScheduler.schedule(timetableTask, zonedExecutionTime.toInstant())
    }
}