package de.twomartens.timetable.bahnApi.service

import de.twomartens.timetable.bahnApi.events.FetchTasksCreatedEvent
import de.twomartens.timetable.bahnApi.model.Eva
import de.twomartens.timetable.bahnApi.model.FetchDates
import de.twomartens.timetable.bahnApi.model.TaskFactory
import de.twomartens.timetable.bahnApi.model.db.ScheduledFetchTask
import de.twomartens.timetable.bahnApi.repository.ScheduledFetchTaskRepository
import de.twomartens.timetable.model.db.TswRoute
import de.twomartens.timetable.types.Hour
import de.twomartens.timetable.types.HourAtDay
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDate

@Service
class ScheduledTaskService(
        private val scheduledFetchTaskRepository: ScheduledFetchTaskRepository,
        private val taskFactory: TaskFactory,
        private val fetchTaskScheduler: FetchTaskScheduler,
        private val mongoTemplate: MongoTemplate,
        private val eventPublisher: ApplicationEventPublisher
) {
    private var createdTime: Instant = Instant.EPOCH
    private var lastUpdate: Instant = Instant.EPOCH

    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationReady(event: ApplicationReadyEvent) {
        log.info { "Application ready" }
        val updateTime = Instant.ofEpochMilli(event.timestamp)
        updateTaskCounterAndScheduleTasks(updateTime)
    }

    @EventListener(ApplicationReadyEvent::class)
    fun onFetchTasksCreated(event: FetchTasksCreatedEvent) {
        log.info { "Scheduled tasks created" }
        val updateTime = Instant.ofEpochMilli(event.timestamp)
        updateTaskCounterAndScheduleTasks(updateTime)
    }

    private fun updateTaskCounterAndScheduleTasks(updateTime: Instant) {
        log.info { "Update tasks from database and schedule" }
        val createdTasks = findTasksCreatedSince(lastUpdate)
        updateCounterIfNotUpToDate(updateTime, createdTasks)
        scheduleTasks(createdTasks)
        lastUpdate = updateTime
    }

    fun triggerTimetableFetch(tswRoute: TswRoute, fetchedDate: LocalDate) {
        log.info {
            "Trigger timetable fetch: [route ${tswRoute.name}]"
        }
        val fetchDates = calculateDatesToFetch(fetchedDate)
        val newTasks = buildScheduledTasks(tswRoute, fetchDates)

        storeTasksInDatabaseAndStoreCreationTime(newTasks)
        val event = FetchTasksCreatedEvent(this)
        eventPublisher.publishEvent(event)
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
        tswRoute.stations.forEach {
            val stationId = it.id
            val eva = Eva.of(stationId)
            var hourAtDay = HourAtDay.of(Hour.of(23), fetchDates.previousDay)
            var newTask = taskFactory.createTaskAndUpdateCounter(tswRoute.userId,
                    tswRoute.routeId, eva, hourAtDay)
            newTasks.add(newTask)
            for (hour in 0..23) {
                hourAtDay = HourAtDay.of(Hour.of(hour), fetchDates.fetchDate)
                newTask = taskFactory.createTaskAndUpdateCounter(tswRoute.userId,
                        tswRoute.routeId, eva, hourAtDay)
                newTasks.add(newTask)
            }
            for (hour in 0..3) {
                hourAtDay = HourAtDay.of(Hour.of(hour), fetchDates.nextDate)
                newTask = taskFactory.createTaskAndUpdateCounter(tswRoute.userId,
                        tswRoute.routeId, eva, hourAtDay)
                newTasks.add(newTask)
            }
        }
        return newTasks
    }

    private fun scheduleTasks(tasksToSchedule: List<ScheduledFetchTask>) {
        fetchTaskScheduler.scheduleFetchTasks(tasksToSchedule)
    }

    private fun updateCounterIfNotUpToDate(updateTime: Instant,
                                           createdTasks: List<ScheduledFetchTask>) {
        if (notUpToDate(updateTime)) {
            taskFactory.updateCounterWith(createdTasks)
        }
    }

    private fun notUpToDate(updateTime: Instant) = createdTime != updateTime

    private fun findTasksCreatedSince(lastScheduledTime: Instant): List<ScheduledFetchTask> {
        return scheduledFetchTaskRepository.findAllByCreatedAfter(lastScheduledTime)
    }

    private fun storeTasksInDatabaseAndStoreCreationTime(newTasks: List<ScheduledFetchTask>) {
        if (newTasks.isNotEmpty()) {
            val firstTask = scheduledFetchTaskRepository.save(newTasks.first())
            createdTime = firstTask.created
        }
        mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,
                ScheduledFetchTask::class.java)
                .insert(newTasks.subList(1, newTasks.size))
                .execute()
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}