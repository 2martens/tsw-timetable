package de.twomartens.timetable.repository

import de.twomartens.timetable.model.NonEmptyString
import de.twomartens.timetable.model.db.TswRoute
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface TswRouteRepository : MongoRepository<TswRoute, ObjectId> {
    fun findByName(name: NonEmptyString): TswRoute?
}
