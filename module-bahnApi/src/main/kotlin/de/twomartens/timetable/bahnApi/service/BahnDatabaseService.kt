package de.twomartens.timetable.bahnApi.service

import de.twomartens.timetable.bahnApi.mapper.BahnStationMapper
import de.twomartens.timetable.bahnApi.mapper.BahnTimetableMapper
import de.twomartens.timetable.bahnApi.model.dto.BahnStation
import de.twomartens.timetable.bahnApi.model.dto.BahnTimetable
import de.twomartens.timetable.bahnApi.repository.BahnStationRepository
import de.twomartens.timetable.bahnApi.repository.BahnTimetableRepository
import de.twomartens.timetable.model.db.Station
import de.twomartens.timetable.model.repository.StationRepository
import de.twomartens.timetable.types.HourAtDay
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class BahnDatabaseService(
        private val bahnStationRepository: BahnStationRepository,
        private val stationRepository: StationRepository,
        private val bahnTimetableRepository: BahnTimetableRepository
) {
    private val bahnStationMapper = Mappers.getMapper(BahnStationMapper::class.java)
    private val bahnTimetableMapper = Mappers.getMapper(BahnTimetableMapper::class.java)

    fun storeStations(stations: List<BahnStation>) {
        val bahnStations = stations.map { bahnStationMapper.mapToDB(it) }
        bahnStationRepository.deleteAll()
        bahnStationRepository.saveAll(bahnStations)

        val existingStations = stationRepository.findAllByCountryCode(COUNTRY_CODE)
        val bahnStationMap = bahnStations.associateBy { it.eva.toString() }
        val commonStationMap = existingStations
                .associateBy { it.stationId.stationIdWithinCountry }

        deleteRemovedStations(existingStations, bahnStationMap)
        updateStations(existingStations, bahnStationMap)
        addNewStations(bahnStations, commonStationMap)
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
            bahnStations: List<de.twomartens.timetable.bahnApi.model.db.BahnStation>,
            commonStationMap: Map<String, Station>
    ) {
        val newStations = bahnStations.filterNot {
            commonStationMap.containsKey(it.eva.toString())
        }.map { bahnStationMapper.mapToCommonDB(it, COUNTRY_CODE) }
        stationRepository.saveAll(newStations)
    }

    fun storeTimetable(timetable: BahnTimetable, hourAtDay: HourAtDay) {
        bahnTimetableRepository.save(bahnTimetableMapper.mapToDB(timetable, hourAtDay))
    }

    companion object {
        private const val COUNTRY_CODE = "de"
    }
}