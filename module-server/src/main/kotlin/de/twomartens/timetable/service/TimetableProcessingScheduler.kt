package de.twomartens.timetable.service

import de.twomartens.timetable.model.db.Timetable
import org.springframework.batch.core.Job
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class TimetableProcessingScheduler(
        private val processTimetableJob: Job,
        private val asyncJobLauncher: JobLauncher,
        private val threadPoolTaskScheduler: ThreadPoolTaskScheduler,
        private val repository: ScheduledProcessTimetableTaskRepository,
        private val clock: Clock,
        private val eventPublisher: ApplicationEventPublisher
) {
    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationReady(event: ApplicationReadyEvent) {
        val tasks = repository.findScheduledProcessTimetableTasksByExecutionTimeAfter(clock.instant())
        tasks.forEach {
            val task = ProcessTimetableTask(asyncJobLauncher, processTimetableJob,
                    it.timetable, repository, it)
            threadPoolTaskScheduler.schedule(task, it.executionTime)
        }
    }

    @EventListener(ProcessTimetableTaskScheduledEvent::class)
    fun onProcessTimetableTaskScheduled(event: ProcessTimetableTaskScheduledEvent) {
        val source = event.source as ScheduledProcessTimetableTask
        val task = ProcessTimetableTask(asyncJobLauncher, processTimetableJob,
                source.timetable, repository, source)
        threadPoolTaskScheduler.schedule(task, source.executionTime)
    }

    fun scheduleTimetableProcessing(timetable: Timetable) {
        val task = ScheduledProcessTimetableTask(timetable.userId, timetable.timetableId,
                timetable, timetable.fetchDate.plusDays(2)
                .atStartOfDay(clock.zone).toInstant())
        repository.save(task)
        val event = ProcessTimetableTaskScheduledEvent(task)
        eventPublisher.publishEvent(event)
    }
}

