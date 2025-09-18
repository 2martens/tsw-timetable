package de.twomartens.timetable.bahnApi.events

import de.twomartens.timetable.bahnApi.tasks.DeleteScheduledTask
import org.springframework.context.ApplicationEvent

class ScheduledTaskDeletedEvent(source: DeleteScheduledTask) : ApplicationEvent(source) {
}