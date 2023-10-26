package de.twomartens.timetable.bahnApi.repository

import de.twomartens.timetable.bahnApi.model.Eva
import de.twomartens.timetable.bahnApi.model.db.ScheduledFetchTask
import de.twomartens.timetable.types.HourAtDay
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ScheduledFetchTaskRepository : MongoRepository<ScheduledFetchTask, ObjectId> {
    fun findByEvaAndFetchedDateTime(eva: Eva, fetchedDateTime: HourAtDay): ScheduledFetchTask?
}
