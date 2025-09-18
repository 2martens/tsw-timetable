package de.twomartens.timetable.service

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.time.ZonedDateTime

@Document
data class StopStorageRequest(
        var userId: String,
        var timetableId: String,
        var eva: String,
        var trainNumber: String,
        var plannedArrivalTime: ZonedDateTime?,
        var plannedDepartureTime: ZonedDateTime?,
        var plannedArrivalPlatform: String?,
        var plannedDeparturePlatform: String?,
        var arrivalLine: String?,
        var departureLine: String?,
        var plannedArrivalPath: String?,
        var plannedDeparturePath: String?) {
    @Id
    lateinit var id: ObjectId

    @CreatedDate
    lateinit var created: Instant

    @LastModifiedDate
    lateinit var lastModified: Instant
}
