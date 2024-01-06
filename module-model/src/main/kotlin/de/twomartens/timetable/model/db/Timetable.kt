package de.twomartens.timetable.model.db

import de.twomartens.timetable.model.common.RouteId
import de.twomartens.timetable.model.common.TimetableId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.model.dto.TimetableState
import de.twomartens.timetable.types.NonEmptyString
import de.twomartens.timetable.types.ZeroOrPositiveInteger
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.time.LocalDate

@Document
@CompoundIndex(def = "{userId: 1, timetableId: 1}", unique = true)
@CompoundIndex(def = "{userId: 1, routeId: 1}")
data class Timetable(
        var userId: UserId,
        var routeId: RouteId,
        var routeName: String,
        var timetableId: TimetableId,
        var name: NonEmptyString,
        var fetchDate: LocalDate,
        var timetableState: TimetableState,
        var numberOfServices: ZeroOrPositiveInteger
) {
    @Id
    lateinit var id: ObjectId

    @CreatedDate
    lateinit var created: Instant

    @LastModifiedDate
    lateinit var lastModified: Instant
}
