package de.twomartens.timetable.model.db

import de.twomartens.timetable.model.common.CountryCode
import de.twomartens.timetable.model.common.Platform
import de.twomartens.timetable.model.common.StationId
import de.twomartens.timetable.types.NonEmptyString
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.TextIndexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
@CompoundIndex(def = "{stationId: 1, countryCode: 1}", unique = true)
data class Station(
        var stationId: StationId,
        var countryCode: CountryCode,
        @TextIndexed var name: NonEmptyString,
        var platforms: List<Platform>
) {
    @Id
    var id: ObjectId = ObjectId()

    @CreatedDate
    lateinit var created: Instant

    @LastModifiedDate
    lateinit var lastModified: Instant
}
