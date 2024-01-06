package de.twomartens.timetable.model.mapper

import de.twomartens.timetable.model.common.CountryCode
import de.twomartens.timetable.model.common.StationId
import de.twomartens.timetable.model.dto.Station
import de.twomartens.timetable.types.NonEmptyString
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
                StationId.of(NonEmptyString(countryCode.countryCode.value + "-" + dto.id)),
                countryCode,
                NonEmptyString(dto.name),
                dto.platforms
        )
    }

    fun mapStationsToDto(stations: List<de.twomartens.timetable.model.db.Station>): List<Station>

    fun mapToDto(db: de.twomartens.timetable.model.db.Station): Station {
        return Station(
                db.stationId.stationIdWithinCountry,
                db.name.value,
                db.platforms
        )
    }
}