package de.twomartens.timetable.bahnApi.service

import de.twomartens.timetable.bahnApi.events.ScheduledTasksCreatedEvent
import de.twomartens.timetable.bahnApi.model.Eva
import de.twomartens.timetable.bahnApi.model.FetchDates
import de.twomartens.timetable.bahnApi.model.TaskFactory
import de.twomartens.timetable.bahnApi.model.db.ScheduledFetchTask
import de.twomartens.timetable.bahnApi.repository.ScheduledFetchTaskRepository
import de.twomartens.timetable.model.db.TswRoute
import de.twomartens.timetable.support.model.LeadershipStatus
import de.twomartens.timetable.support.service.BusService
import de.twomartens.timetable.types.Hour
import de.twomartens.timetable.types.HourAtDay
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.cloud.kubernetes.commons.leader.LeaderProperties
import org.springframework.context.event.EventListener
import org.springframework.integration.leader.event.OnGrantedEvent
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDate

@Service
class ScheduledTaskService(
        private val busService: BusService,
        private val leadershipStatus: LeadershipStatus,
        private val leaderProperties: LeaderProperties,
        private val scheduledFetchTaskRepository: ScheduledFetchTaskRepository,
        private val taskFactory: TaskFactory,
        private val fetchTaskScheduler: FetchTaskScheduler
) {
    private var createdTime: Instant = Instant.EPOCH
    private var lastUpdate: Instant = Instant.EPOCH

    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationReady(event: ApplicationReadyEvent) {
        if (!leaderProperties.isEnabled) {
            val updateTime = Instant.ofEpochMilli(event.timestamp)
            updateTaskCounterAndScheduleTasksIfLeader(updateTime)
        }
    }

    @EventListener(OnGrantedEvent::class)
    fun onLeadershipGranted(event: OnGrantedEvent) {
        val updateTime = Instant.ofEpochMilli(event.timestamp)
        updateTaskCounterAndScheduleTasksIfLeader(updateTime)
    }

    @EventListener(ScheduledTasksCreatedEvent::class)
    fun onScheduledTasksCreated(event: ScheduledTasksCreatedEvent) {
        updateTaskCounterAndScheduleTasksIfLeader(event.source)
    }

    private fun updateTaskCounterAndScheduleTasksIfLeader(updateTime: Instant) {
        log.info { "Update tasks from database and schedule if leader" }
        val createdTasks = findTasksCreatedSince(lastUpdate)
        updateCounterIfNotUpToDate(updateTime, createdTasks)
        scheduleTasksIfLeader(createdTasks)
        lastUpdate = updateTime
    }

    fun triggerTimetableFetch(tswRoute: TswRoute, fetchedDate: LocalDate) {
        log.info {
            "Trigger timetable fetch: [route ${tswRoute.name}]"
        }
        val fetchDates = calculateDatesToFetch(fetchedDate)
        val newTasks = buildScheduledTasks(tswRoute, fetchDates)

        storeTasksInDatabaseAndStoreCreationTime(newTasks)
        publishTasksCreatedEvent()
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

    private fun scheduleTasksIfLeader(tasksToSchedule: List<ScheduledFetchTask>) {
        if (leadershipStatus.isLeader) {
            fetchTaskScheduler.scheduleFetchTasks(tasksToSchedule)
        }
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
        val savedEntities = scheduledFetchTaskRepository.saveAll(newTasks)
        storeCreationTime(savedEntities)
    }

    private fun storeCreationTime(savedEntities: MutableList<ScheduledFetchTask>) {
        if (savedEntities.isNotEmpty()) {
            createdTime = savedEntities[savedEntities.lastIndex].created
        }
    }

    private fun publishTasksCreatedEvent() {
        val event = ScheduledTasksCreatedEvent.of(createdTime)
        busService.publishEvent(event)
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}