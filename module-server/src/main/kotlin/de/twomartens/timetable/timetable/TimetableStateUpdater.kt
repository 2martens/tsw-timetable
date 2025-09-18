package de.twomartens.timetable.timetable

import de.twomartens.timetable.bahnApi.events.ScheduledTaskDeletedEvent
import de.twomartens.timetable.bahnApi.repository.ScheduledFetchTaskRepository
import de.twomartens.timetable.bahnApi.tasks.DeleteScheduledTask
import de.twomartens.timetable.model.dto.TimetableState
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class TimetableStateUpdater(
        private val fetchTaskRepository: ScheduledFetchTaskRepository,
        private val timetableRepository: TimetableRepository
) {

    @EventListener(ScheduledTaskDeletedEvent::class)
    fun onScheduledTaskDeleted(event: ScheduledTaskDeletedEvent) {
        val source = event.source as DeleteScheduledTask
        val tasks = fetchTaskRepository.findScheduledFetchTaskByUserIdAndTswTimetableId(source.scheduledFetchTask.userId,
                source.scheduledFetchTask.tswTimetableId)
        if (tasks.isEmpty()) {
            val timetable = timetableRepository.findByUserIdAndTimetableId(source.scheduledFetchTask.userId,
                    source.scheduledFetchTask.tswTimetableId)
            timetable?.timetableState = TimetableState.TIMETABLES_FETCHED
            if (timetable != null) {
                timetableRepository.save(timetable)
            }
        }
    }
}