package de.twomartens.timetable.model.db

import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.types.Email
import de.twomartens.timetable.types.NonEmptyString
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
data class User(
        @Indexed(unique = true) var userId: UserId,
        var name: NonEmptyString,
        var email: Email
) {
    @Id
    var id: ObjectId = ObjectId()

    @CreatedDate
    lateinit var created: Instant

    @LastModifiedDate
    lateinit var lastModified: Instant
}
