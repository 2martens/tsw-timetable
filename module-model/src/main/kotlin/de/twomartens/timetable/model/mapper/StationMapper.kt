package de.twomartens.timetable.model.mapper

import de.twomartens.timetable.model.common.CountryCode
import de.twomartens.timetable.model.dto.Station
import org.mapstruct.*

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface StationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    fun mapToDB(countryCode: CountryCode, dto: Station): de.twomartens.timetable.model.db.Station {
        return de.twomartens.timetable.model.db.Station(
                dto.id,
                countryCode,
                dto.name,
                dto.platforms
        )
    }

    fun mapStationsToDto(stations: List<de.twomartens.timetable.model.db.Station>): List<Station>

    fun mapToDto(db: de.twomartens.timetable.model.db.Station): Station {
        return Station(
                db.stationId,
                db.name,
                db.platforms
        )
    }
}