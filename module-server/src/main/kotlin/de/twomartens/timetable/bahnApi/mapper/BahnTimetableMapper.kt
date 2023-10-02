package de.twomartens.timetable.bahnApi.mapper

import de.twomartens.timetable.bahnApi.model.db.BahnTimetable
import org.mapstruct.*
import java.time.LocalDateTime

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
                dateTime: LocalDateTime): BahnTimetable {
        return BahnTimetable(
                dto.eva!!,
                dateTime,
                dto.station,
                dto.stops
        )
    }
}