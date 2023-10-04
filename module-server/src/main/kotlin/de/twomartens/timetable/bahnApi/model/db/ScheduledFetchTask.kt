package de.twomartens.timetable.bahnApi.model.db

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Document
@CompoundIndex(def = "{'eva': 1, 'fetchedDate': 1, 'fetchedTime': 1", unique = true)
data class ScheduledFetchTask(
        var eva: Int,
        var fetchedDate: LocalDate,
        var fetchedTime: LocalTime,
        var scheduledExecutionDateTime: LocalDateTime
) {
    @Id
    var id: ObjectId = ObjectId()

    @CreatedDate
    lateinit var created: Instant

    @LastModifiedDate
    lateinit var lastModified: Instant
}
