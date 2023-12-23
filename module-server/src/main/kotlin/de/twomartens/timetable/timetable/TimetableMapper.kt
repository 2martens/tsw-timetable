package de.twomartens.timetable.timetable

import de.twomartens.timetable.model.common.RouteId
import de.twomartens.timetable.model.common.TimetableId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.model.dto.Timetable
import de.twomartens.timetable.types.NonEmptyString
import de.twomartens.timetable.types.ZeroOrPositiveInteger
import org.mapstruct.*

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface TimetableMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    fun mapToDB(userId: UserId, dto: Timetable): de.twomartens.timetable.model.db.Timetable {
        return de.twomartens.timetable.model.db.Timetable(
                userId,
                RouteId.of(NonEmptyString(dto.routeId)),
                TimetableId.of(NonEmptyString(dto.id)),
                NonEmptyString(dto.name),
                dto.date,
                dto.state,
                ZeroOrPositiveInteger(dto.numberOfServices)
        )
    }

    fun mapTimetablesToDto(routes: List<de.twomartens.timetable.model.db.Timetable>): List<Timetable>

    fun mapToDto(db: de.twomartens.timetable.model.db.Timetable): Timetable {
        return Timetable(
                db.timetableId.value,
                db.name.value,
                db.routeId.id,
                db.fetchDate,
                db.timetableState,
                db.numberOfServices.value
        )
    }
}