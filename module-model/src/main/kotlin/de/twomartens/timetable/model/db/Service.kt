package de.twomartens.timetable.model.db

import de.twomartens.timetable.model.common.ServiceId
import de.twomartens.timetable.model.common.TimetableId
import de.twomartens.timetable.model.common.UserId
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
@CompoundIndex(def = "{'userId': 1, 'timetableId': 1, 'serviceId': 1}", unique = true)
data class Service(
        var userId: UserId,
        var timetableId: TimetableId,
        var serviceId: ServiceId,
        private var stops: MutableList<ServiceStop> = mutableListOf()
) {
    @Id
    lateinit var id: ObjectId

    @CreatedDate
    lateinit var created: Instant

    @LastModifiedDate
    lateinit var lastModified: Instant

    fun addStop(stop: ServiceStop) {
        stops.add(stop)
        stops.sortWith(compareBy { it.plannedDepartureTime ?: it.plannedArrivalTime })
    }
}