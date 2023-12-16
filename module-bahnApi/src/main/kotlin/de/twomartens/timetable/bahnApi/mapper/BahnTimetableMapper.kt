package de.twomartens.timetable.bahnApi.mapper

import de.twomartens.timetable.bahnApi.model.db.BahnTimetable
import de.twomartens.timetable.types.HourAtDay
import de.twomartens.timetable.types.NonEmptyString
import org.mapstruct.*

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface BahnTimetableMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    fun mapToDB(dto: de.twomartens.timetable.bahnApi.model.dto.BahnTimetable,
                hourAtDay: HourAtDay): BahnTimetable {
        return BahnTimetable(
                dto.eva,
                hourAtDay,
                NonEmptyString(dto.station),
                dto.stops
        )
    }
}