package de.twomartens.timetable.timetable

import de.twomartens.timetable.model.common.TimetableId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.model.db.Timetable
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface TimetableRepository : MongoRepository<Timetable, ObjectId> {
    fun findByUserIdAndTimetableId(userId: UserId, timetableId: TimetableId): Timetable?
    fun findAllByUserIdAndNameContainingIgnoreCase(userId: UserId, name: String): List<Timetable>
    fun findAllByUserId(userId: UserId): List<Timetable>
}