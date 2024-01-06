package de.twomartens.timetable.model.db

import de.twomartens.timetable.model.common.RouteId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.model.dto.Country
import de.twomartens.timetable.model.dto.Station
import de.twomartens.timetable.types.NonEmptyString
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
        var routeId: RouteId,
        var name: NonEmptyString,
        var country: Country,
        var stations: List<Station>,
        var depots: List<Depot>,
        var portals: List<Portal>
) {
    @Id
    lateinit var id: ObjectId

    @CreatedDate
    lateinit var created: Instant

    @LastModifiedDate
    lateinit var lastModified: Instant
}
