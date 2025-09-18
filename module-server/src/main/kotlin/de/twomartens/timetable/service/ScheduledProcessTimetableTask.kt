package de.twomartens.timetable.service

import de.twomartens.timetable.model.common.TimetableId
import de.twomartens.timetable.model.common.UserId
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
@CompoundIndex(def = "{'userId': 1, 'tswTimetableId': 1}", unique = true)
data class ScheduledProcessTimetableTask(
        var userId: UserId,
        var timetableId: TimetableId,
        var executionTime: Instant
) {
    @Id
    lateinit var id: ObjectId

    @CreatedDate
    lateinit var created: Instant

    @LastModifiedDate
    lateinit var lastModified: Instant
}
