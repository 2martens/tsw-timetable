package de.twomartens.timetable.bahnApi.service

import de.twomartens.timetable.bahnApi.model.db.ScheduledFetchTask
import de.twomartens.timetable.bahnApi.model.dto.BahnTimetable
import de.twomartens.timetable.bahnApi.repository.ScheduledFetchTaskRepository
import de.twomartens.timetable.bahnApi.tasks.DeleteScheduledTask
import de.twomartens.timetable.bahnApi.tasks.FetchTimetableTask
import de.twomartens.timetable.bahnApi.tasks.StoreTimetableTask
import de.twomartens.timetable.model.common.RouteId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.types.HourAtDay
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.ZonedDateTime

private const val PAST_TASK_EXECUTION_OFFSET = 1

@Service
class FetchTaskScheduler(
        private val clock: Clock,
        private val threadPoolTaskScheduler: ThreadPoolTaskScheduler,
        private val threadPoolTaskExecutor: ThreadPoolTaskExecutor,
        private val bahnApiService: BahnApiService,
        private val bahnDatabaseService: BahnDatabaseService,
        private val scheduledFetchTaskRepository: ScheduledFetchTaskRepository
) {
    fun scheduleStoreTask(timetable: BahnTimetable, userId: UserId, routeId: RouteId,
                          hourAtDay: HourAtDay) {
        val storeTask = StoreTimetableTask(timetable, userId, routeId, hourAtDay, bahnDatabaseService)
        threadPoolTaskExecutor.execute(storeTask)
    }

    fun scheduleFetchTasks(scheduledTasks: List<ScheduledFetchTask>) {
        val nowPlus1Minute = ZonedDateTime.now(clock).plusMinutes(1)
        scheduleTasksFromThePast(scheduledTasks, nowPlus1Minute)
        scheduleTasksFromTheFuture(scheduledTasks, nowPlus1Minute)
    }

    private fun scheduleTasksFromThePast(
            scheduledTasks: List<ScheduledFetchTask>,
            nowPlus1Minute: ZonedDateTime
    ) {
        val tasksScheduledForPast = scheduledTasks.filter {
            val zonedExecutionTime = it.scheduledExecutionDateTime.atZone(clock.zone)
            nowPlus1Minute.isAfter(zonedExecutionTime)
        }
        tasksScheduledForPast.forEach {
            val zonedExecutionTime = it.scheduledExecutionDateTime.atZone(clock.zone)
            val adjustedExecutionTime = nowPlus1Minute.withOffsetAndSecondsFrom(zonedExecutionTime)
            scheduleDeleteTask(it, adjustedExecutionTime)
            scheduleFetchTimetableTask(it, adjustedExecutionTime)
        }
    }

    private fun scheduleTasksFromTheFuture(
            scheduledTasks: List<ScheduledFetchTask>,
            nowPlus1Minute: ZonedDateTime
    ) {
        val tasksScheduledForFuture = scheduledTasks.filter {
            val zonedExecutionTime = it.scheduledExecutionDateTime.atZone(clock.zone)
            nowPlus1Minute.isOnOrBefore(zonedExecutionTime)
        }
        tasksScheduledForFuture.forEach {
            val zonedExecutionTime = it.scheduledExecutionDateTime.atZone(clock.zone)
            scheduleDeleteTask(it, zonedExecutionTime)
            scheduleFetchTimetableTask(it, zonedExecutionTime)
        }
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
        val timetableTask = FetchTimetableTask(scheduledFetchTask.userId, scheduledFetchTask.routeId,
                scheduledFetchTask.eva, scheduledFetchTask.fetchedDateTime,
                bahnApiService, this)
        threadPoolTaskScheduler.schedule(timetableTask, zonedExecutionTime.toInstant())
    }

    companion object {
        fun ZonedDateTime.withOffsetAndSecondsFrom(other: ZonedDateTime): ZonedDateTime =
                this.withSecond(other.second + PAST_TASK_EXECUTION_OFFSET)

        fun ZonedDateTime.isOnOrBefore(other: ZonedDateTime): Boolean =
                this.isBefore(other) || this.isEqual(other)
    }
}
