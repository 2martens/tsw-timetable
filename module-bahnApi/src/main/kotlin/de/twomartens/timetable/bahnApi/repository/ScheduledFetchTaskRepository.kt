package de.twomartens.timetable.bahnApi.repository

import de.twomartens.timetable.bahnApi.model.Eva
import de.twomartens.timetable.bahnApi.model.db.ScheduledFetchTask
import de.twomartens.timetable.types.HourAtDay
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant

interface ScheduledFetchTaskRepository : MongoRepository<ScheduledFetchTask, ObjectId> {
    fun findByEvaAndFetchedDateTime(eva: Eva, fetchedDateTime: HourAtDay): ScheduledFetchTask?

    fun findAllByCreatedAfter(createdAt: Instant): List<ScheduledFetchTask>
}
