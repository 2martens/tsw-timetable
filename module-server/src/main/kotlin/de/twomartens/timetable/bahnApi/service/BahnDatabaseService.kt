package de.twomartens.timetable.bahnApi.service

import de.twomartens.timetable.bahnApi.mapper.BahnStationMapper
import de.twomartens.timetable.bahnApi.model.dto.BahnStation
import de.twomartens.timetable.bahnApi.repository.BahnStationRepository
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class BahnDatabaseService(
        private val bahnStationRepository: BahnStationRepository
) {
    private val bahnStationMapper = Mappers.getMapper(BahnStationMapper::class.java)

    fun storeStations(stations: List<BahnStation>) {
        bahnStationRepository.saveAll(stations
                .map { bahnStationMapper.mapToDB(it) })
    }
}