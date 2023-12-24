package de.twomartens.timetable.model.repository

import de.twomartens.timetable.model.common.StationId
import de.twomartens.timetable.model.db.Station
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository

interface StationRepository : MongoRepository<Station, ObjectId> {
    fun findByCountryCodeAndStationId(countryCode: String, stationId: StationId): Station?
    fun findAllByCountryCode(countryCode: String): List<Station>

    @Aggregation(pipeline = [
        "{\$search: { index: \"stations\",compound: {must: [{phrase: {query: ?0, path: \"countryCode\"}},{autocomplete: {query: ?1,path: \"name\",tokenOrder: \"sequential\"}}]}}}"
    ])
    fun findAllByCountryCodeAndNameContainingIgnoreCase(countryCode: String, name: String): List<Station>

    @Aggregation(pipeline = [
        "{\$search: { index: \"stations\",autocomplete: {query: ?0, path: \"name\",tokenOrder: \"sequential\"}}}"
    ])
    fun findAllByNameContainingIgnoreCase(name: String): List<Station>
}