package de.twomartens.timetable.model.db

import de.twomartens.timetable.model.Eva
import de.twomartens.timetable.model.NonEmptyString
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
data class TswRoute(
        @Indexed(unique = true) var name: NonEmptyString,
        /**
         * Ordered list of stations that represent the order of stations on the route.
         *
         * Not every stop is necessarily called at by each train. There may be multiple
         * parallel paths through a route (like S-Bahn track and long-distance track).
         * In such cases, the S-Bahn stops are listed as part of the route as they appear
         * geographically.
         *
         * Only the station ids are used here as the duplication would be heavy and
         * consistency hard to ensure when potentially repeated across various routes.
         */
        var stations: List<Eva>
) {
    @Id
    var id: ObjectId = ObjectId()

    @CreatedDate
    lateinit var created: Instant

    @LastModifiedDate
    lateinit var lastModified: Instant
}
