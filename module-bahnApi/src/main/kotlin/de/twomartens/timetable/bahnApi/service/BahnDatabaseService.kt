package de.twomartens.timetable.bahnApi.service

import de.twomartens.timetable.bahnApi.mapper.BahnStationMapper
import de.twomartens.timetable.bahnApi.mapper.BahnTimetableMapper
import de.twomartens.timetable.bahnApi.model.dto.BahnStation
import de.twomartens.timetable.bahnApi.model.dto.BahnTimetable
import de.twomartens.timetable.bahnApi.repository.BahnStationRepository
import de.twomartens.timetable.bahnApi.repository.BahnTimetableRepository
import de.twomartens.timetable.model.common.RouteId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.model.db.Station
import de.twomartens.timetable.model.repository.StationRepository
import de.twomartens.timetable.types.HourAtDay
import org.mapstruct.factory.Mappers
import org.springframework.data.domain.Example
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service

@Service
open class BahnDatabaseService(
        private val bahnStationRepository: BahnStationRepository,
        private val stationRepository: StationRepository,
        private val bahnTimetableRepository: BahnTimetableRepository,
        private val mongoTemplate: MongoTemplate
) {
    private val bahnStationMapper = Mappers.getMapper(BahnStationMapper::class.java)
    private val bahnTimetableMapper = Mappers.getMapper(BahnTimetableMapper::class.java)

    fun storeStations(stations: List<BahnStation>) {

        val existingStations = stationRepository.findAllByCountryCode(COUNTRY_CODE)
        val commonStationMap = existingStations
                .associateBy { it.stationId.stationIdWithinCountry }
        val bahnStationMap = stations.asSequence()
                .map(bahnStationMapper::mapToDB)
                .associateBy { it.eva.toString() }

        updateBahnStations(bahnStationMap)
        deleteRemovedStations(existingStations, bahnStationMap)
        updateStations(existingStations, bahnStationMap)
        addNewStations(bahnStationMap, commonStationMap)
    }

    private fun updateBahnStations(bahnStationMap: Map<String, de.twomartens.timetable.bahnApi.model.db.BahnStation>) {
        bahnStationRepository.deleteAll()
        val bahnStations: List<de.twomartens.timetable.bahnApi.model.db.BahnStation> = buildList {
            this.addAll(bahnStationMap.values)
        }

        mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,
                de.twomartens.timetable.bahnApi.model.db.BahnStation::class.java)
                .insert(bahnStations)
                .execute()
    }

    private fun deleteRemovedStations(
            existingStations: List<Station>,
            bahnStationMap: Map<String, de.twomartens.timetable.bahnApi.model.db.BahnStation>
    ) {
        val deletedStations = existingStations
                .filterNot { bahnStationMap.containsKey(it.stationId.stationIdWithinCountry) }
        stationRepository.deleteAll(deletedStations)
    }

    private fun updateStations(
            existingStations: List<Station>,
            bahnStationMap: Map<String, de.twomartens.timetable.bahnApi.model.db.BahnStation>
    ) {
        val updatedStations = existingStations
                .filter { bahnStationMap.containsKey(it.stationId.stationIdWithinCountry) }
        updatedStations.map {
            it.name = bahnStationMap[it.stationId.stationIdWithinCountry]!!.name
        }
        stationRepository.saveAll(updatedStations)
    }

    private fun addNewStations(
            bahnStations: Map<String, de.twomartens.timetable.bahnApi.model.db.BahnStation>,
            commonStationMap: Map<String, Station>
    ) {
        val newStations = bahnStations.asSequence()
                .filterNot { commonStationMap.containsKey(it.key) }
                .map { bahnStationMapper.mapToCommonDB(it.value, COUNTRY_CODE) }
                .toList()

        mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,
                Station::class.java)
                .insert(newStations)
                .execute()
    }

    fun storeTimetable(timetable: BahnTimetable, userId: UserId, routeId: RouteId,
                       hourAtDay: HourAtDay) {
        val matcher = bahnTimetableRepository.getExampleMatcher()
        val dbTimetable = bahnTimetableMapper.mapToDB(timetable, userId, routeId, hourAtDay)
        val existingTimetable = bahnTimetableRepository.findOne(Example.of(dbTimetable, matcher))
        existingTimetable
                .map {
                    it.stops = dbTimetable.stops
                    it.station = dbTimetable.station
                    it
                }
                .orElse(dbTimetable)
        bahnTimetableRepository.save(dbTimetable)
    }

    companion object {
        private const val COUNTRY_CODE = "de"
    }
}