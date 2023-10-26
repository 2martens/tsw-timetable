package de.twomartens.timetable.bahnApi.service

import de.twomartens.timetable.bahnApi.mapper.BahnStationMapper
import de.twomartens.timetable.bahnApi.mapper.BahnTimetableMapper
import de.twomartens.timetable.bahnApi.model.dto.BahnStation
import de.twomartens.timetable.bahnApi.model.dto.BahnTimetable
import de.twomartens.timetable.bahnApi.repository.BahnStationRepository
import de.twomartens.timetable.bahnApi.repository.BahnTimetableRepository
import de.twomartens.timetable.model.base.HourAtDay
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class BahnDatabaseService(
        private val bahnStationRepository: BahnStationRepository,
        private val bahnTimetableRepository: BahnTimetableRepository
) {
    private val bahnStationMapper = Mappers.getMapper(BahnStationMapper::class.java)
    private val bahnTimetableMapper = Mappers.getMapper(BahnTimetableMapper::class.java)

    fun storeStations(stations: List<BahnStation>) {
        bahnStationRepository.saveAll(stations
                .map { bahnStationMapper.mapToDB(it) })
    }

    fun storeTimetable(timetable: BahnTimetable, hourAtDay: HourAtDay) {
        bahnTimetableRepository.save(bahnTimetableMapper.mapToDB(timetable, hourAtDay.dateTime))
    }
}