package de.twomartens.timetable.bahnApi.events

import org.springframework.cloud.bus.event.Destination
import org.springframework.cloud.bus.event.PathDestinationFactory
import org.springframework.cloud.bus.event.RemoteApplicationEvent
import java.time.Instant

private const val TIMETABLE_SERVICE_NAME = "timetable"

class ScheduledTasksCreatedEvent private constructor(source: Instant, originService: String,
                                                     destination: Destination)
    : RemoteApplicationEvent(source, originService, destination) {

    private val creationTime: Instant

    init {
        creationTime = source
    }

    override fun getSource(): Instant {
        return creationTime
    }

    companion object {
        private val destinationFactory = PathDestinationFactory()

        fun of(source: Instant): ScheduledTasksCreatedEvent {
            return ScheduledTasksCreatedEvent(source,
                    TIMETABLE_SERVICE_NAME,
                    destinationFactory.getDestination(TIMETABLE_SERVICE_NAME))
        }
    }
}