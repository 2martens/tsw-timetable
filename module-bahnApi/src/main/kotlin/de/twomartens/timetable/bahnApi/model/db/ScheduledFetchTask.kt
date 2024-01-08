package de.twomartens.timetable.bahnApi.model.db

import de.twomartens.timetable.bahnApi.model.Eva
import de.twomartens.timetable.model.common.RouteId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.types.HourAtDay
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.time.LocalDateTime

@Document
@CompoundIndex(def = "{'userId': 1, 'routeId': 1, 'eva': 1, 'fetchedDateTime': 1}", unique = true)
data class ScheduledFetchTask(
        var userId: UserId,
        var routeId: RouteId,
        var eva: Eva,
        var fetchedDateTime: HourAtDay,
        var scheduledExecutionDateTime: LocalDateTime
) {
    @Id
    lateinit var id: ObjectId

    @CreatedDate
    lateinit var created: Instant

    @LastModifiedDate
    lateinit var lastModified: Instant
}
