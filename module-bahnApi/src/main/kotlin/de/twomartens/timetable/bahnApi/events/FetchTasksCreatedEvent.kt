package de.twomartens.timetable.bahnApi.events

import org.springframework.context.ApplicationEvent

class FetchTasksCreatedEvent(source: Any) : ApplicationEvent(source)