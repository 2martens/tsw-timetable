package de.twomartens.timetable.bahnApi.repository

import de.twomartens.timetable.bahnApi.model.Eva
import de.twomartens.timetable.bahnApi.model.db.BahnStation
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface BahnStationRepository : MongoRepository<BahnStation, ObjectId> {
    fun findByEva(eva: Eva): BahnStation?
}
