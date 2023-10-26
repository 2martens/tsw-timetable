package de.twomartens.timetable.bahnApi.mapper

import de.twomartens.timetable.bahnApi.model.db.BahnStation
import de.twomartens.timetable.types.NonEmptyString
import org.mapstruct.*

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface BahnStationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    fun mapToDB(dto: de.twomartens.timetable.bahnApi.model.dto.BahnStation): BahnStation {
        return BahnStation(
                dto.eva,
                NonEmptyString(dto.name),
                dto.ds100,
                dto.db
        )
    }
}