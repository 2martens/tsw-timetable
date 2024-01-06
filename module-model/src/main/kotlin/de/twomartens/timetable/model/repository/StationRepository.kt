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
        "{\$search: { index: \"stations\", returnStoredSource: true, compound: {must: [{phrase: {query: ?0, path: \"countryCode\"}},{autocomplete: {query: ?1,path: \"name\",tokenOrder: \"sequential\"}}]}}}",
        "{\$limit: 10}",
        "{\$lookup: { from: \"station\", localField: \"_id\", foreignField: \"_id\", as: \"document\" }}",
        "{\$unwind: \"\$document\"}",
        "{\$replaceWith: \"\$document\"}"
    ])
    fun findAllByCountryCodeAndNameContainingIgnoreCase(countryCode: String, name: String): List<Station>

    @Aggregation(pipeline = [
        "{\$search: { index: \"stations\", returnStoredSource: true, autocomplete: {query: ?0, path: \"name\",tokenOrder: \"sequential\"}}}",
        "{\$limit: 10}",
        "{\$lookup: { from: \"station\", localField: \"_id\", foreignField: \"_id\", as: \"document\" }}",
        "{\$unwind: \"\$document\"}",
        "{\$replaceWith: \"\$document\"}"
    ])
    fun findAllByNameContainingIgnoreCase(name: String): List<Station>
}