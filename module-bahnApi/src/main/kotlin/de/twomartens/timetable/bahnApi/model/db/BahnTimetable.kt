package de.twomartens.timetable.bahnApi.model.db

import de.twomartens.timetable.bahnApi.model.Eva
import de.twomartens.timetable.model.common.TimetableId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.types.NonEmptyString
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.time.ZonedDateTime

@Document
@CompoundIndex(def = "{'userId': 1, 'tswTimetableId': 1, 'eva': 1, 'hourAtDay': 1}",
        unique = true)
data class BahnTimetable(
        var userId: UserId,
        var tswTimetableId: TimetableId,
        var eva: Eva,
        var hourAtDay: ZonedDateTime,
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