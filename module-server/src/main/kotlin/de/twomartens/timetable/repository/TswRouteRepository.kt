package de.twomartens.timetable.repository

import de.twomartens.timetable.model.base.NonEmptyString
import de.twomartens.timetable.model.db.TswRoute
import de.twomartens.timetable.model.db.UserId
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface TswRouteRepository : MongoRepository<TswRoute, ObjectId> {
    fun findByUserIdAndName(userId: UserId, name: NonEmptyString): TswRoute?
}
