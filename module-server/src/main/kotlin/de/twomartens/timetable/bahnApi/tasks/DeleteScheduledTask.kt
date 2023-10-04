package de.twomartens.timetable.bahnApi.tasks

import de.twomartens.timetable.bahnApi.model.db.ScheduledFetchTask
import de.twomartens.timetable.bahnApi.repository.ScheduledFetchTaskRepository
import org.springframework.scheduling.annotation.Async

@Async
open class DeleteScheduledTask(
        private val scheduledFetchTaskRepository: ScheduledFetchTaskRepository,
        private val scheduledFetchTask: ScheduledFetchTask
) : Runnable {
    override fun run() {
        scheduledFetchTaskRepository.delete(scheduledFetchTask)
    }
}