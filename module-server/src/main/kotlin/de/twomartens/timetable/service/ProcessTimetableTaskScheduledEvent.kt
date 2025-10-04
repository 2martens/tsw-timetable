package de.twomartens.timetable.service

import org.springframework.context.ApplicationEvent

class ProcessTimetableTaskScheduledEvent(source: ScheduledProcessTimetableTask) : ApplicationEvent(source) {
}