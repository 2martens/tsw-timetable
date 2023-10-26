package de.twomartens.timetable.model.db

import de.twomartens.timetable.model.base.NonEmptyString
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
@CompoundIndex(def = "{'userId': 1, 'name': 1}", unique = true)
data class TswRoute(
        var userId: UserId,
        var name: NonEmptyString,
        var countryCode: CountryCode,
        var stationIds: List<StationId>,
        var portals: List<Portal>,
        var depots: List<Depot>
) {
    @Id
    var id: ObjectId = ObjectId()

    @CreatedDate
    lateinit var created: Instant

    @LastModifiedDate
    lateinit var lastModified: Instant
}
