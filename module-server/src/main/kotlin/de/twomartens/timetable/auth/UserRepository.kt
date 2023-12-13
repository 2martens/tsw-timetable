package de.twomartens.timetable.auth

import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.model.db.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, ObjectId> {
    fun existsByUserId(userId: UserId): Boolean
}