package de.twomartens.timetable.bahnApi.model.db

import de.twomartens.timetable.bahnApi.model.Eva
import de.twomartens.timetable.types.NonEmptyString
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.index.TextIndexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
data class BahnStation(
        @Indexed(unique = true) var eva: Eva,
        @TextIndexed var name: NonEmptyString,
        var ds100: String,
        var db: Boolean
) {
    @Id
    lateinit var id: ObjectId

    @CreatedDate
    lateinit var created: Instant

    @LastModifiedDate
    lateinit var lastModified: Instant
}