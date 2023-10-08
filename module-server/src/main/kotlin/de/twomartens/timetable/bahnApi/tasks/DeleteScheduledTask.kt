package de.twomartens.timetable.bahnApi.tasks

import de.twomartens.timetable.bahnApi.model.db.ScheduledFetchTask
import de.twomartens.timetable.bahnApi.repository.ScheduledFetchTaskRepository
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Async

@Async
open class DeleteScheduledTask(
        private val scheduledFetchTaskRepository: ScheduledFetchTaskRepository,
        private val scheduledFetchTask: ScheduledFetchTask
) : Runnable {
    override fun run() {
        log.info {
            "Delete scheduled fetch task after the fetch occurred: " +
                    "[eva: ${scheduledFetchTask.eva}], " +
                    "[date: ${scheduledFetchTask.fetchedDate}], " +
                    "[time: ${scheduledFetchTask.fetchedTime}]"
        }
        scheduledFetchTaskRepository.delete(scheduledFetchTask)
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}