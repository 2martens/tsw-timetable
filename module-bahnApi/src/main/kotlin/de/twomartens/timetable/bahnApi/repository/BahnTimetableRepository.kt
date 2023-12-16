package de.twomartens.timetable.bahnApi.repository

import de.twomartens.timetable.bahnApi.model.Eva
import de.twomartens.timetable.bahnApi.model.db.BahnTimetable
import de.twomartens.timetable.types.HourAtDay
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

// TODO: figure out what ID type should be
interface BahnTimetableRepository : MongoRepository<BahnTimetable, ObjectId> {
    fun findByEvaAndHourAtDay(eva: Eva, hourAtDay: HourAtDay): BahnTimetable?
}