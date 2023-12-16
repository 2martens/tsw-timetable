package de.twomartens.timetable.route

import de.twomartens.timetable.model.common.RouteId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.model.db.TswRoute
import de.twomartens.timetable.types.NonEmptyString
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface TswRouteRepository : MongoRepository<TswRoute, ObjectId> {
    fun findByUserIdAndRouteId(userId: UserId, routeId: RouteId): TswRoute?
    fun findByUserIdAndName(userId: UserId, name: NonEmptyString): TswRoute?
}
