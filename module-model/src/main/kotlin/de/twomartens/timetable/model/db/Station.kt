package de.twomartens.timetable.model.db

import de.twomartens.timetable.model.common.Platform
import de.twomartens.timetable.model.common.StationId
import de.twomartens.timetable.types.NonEmptyString
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
data class Station(
        @Indexed(unique = true) var stationId: StationId,
        var name: NonEmptyString,
        var platforms: List<Platform>
) {
    @Id
    var id: ObjectId = ObjectId()

    @CreatedDate
    lateinit var created: Instant

    @LastModifiedDate
    lateinit var lastModified: Instant
}
