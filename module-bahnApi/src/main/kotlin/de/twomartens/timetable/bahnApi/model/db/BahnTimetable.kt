package de.twomartens.timetable.bahnApi.model.db

import de.twomartens.timetable.bahnApi.model.Eva
import de.twomartens.timetable.bahnApi.model.dto.BahnStationStop
import de.twomartens.timetable.model.common.RouteId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.types.HourAtDay
import de.twomartens.timetable.types.NonEmptyString
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
@CompoundIndex(def = "{'userId': 1, 'routeId': 1, 'eva': 1, 'hourAtDay': 1}", unique = true)
data class BahnTimetable(
        var userId: UserId,
        var routeId: RouteId,
        var eva: Eva,
        var hourAtDay: HourAtDay,
        var station: NonEmptyString,
        var stops: List<BahnStationStop>
) {
    @Id
    lateinit var id: ObjectId

    @CreatedDate
    lateinit var created: Instant

    @LastModifiedDate
    lateinit var lastModified: Instant
}