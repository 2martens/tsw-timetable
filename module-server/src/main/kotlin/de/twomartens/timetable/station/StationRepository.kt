package de.twomartens.timetable.station

import de.twomartens.timetable.model.common.StationId
import de.twomartens.timetable.model.db.Station
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface StationRepository : MongoRepository<Station, ObjectId> {
    fun findByCountryCodeAndStationId(countryCode: String, stationId: StationId): Station?
    fun findAllByCountryCodeAndNameContainingIgnoreCase(countryCode: String, name: String): List<Station>
    fun findAllByNameContainingIgnoreCase(name: String): List<Station>
}