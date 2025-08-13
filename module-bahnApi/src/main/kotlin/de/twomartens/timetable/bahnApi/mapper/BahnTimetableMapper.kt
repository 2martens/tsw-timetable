package de.twomartens.timetable.bahnApi.mapper

import de.twomartens.timetable.bahnApi.model.db.BahnStationStop
import de.twomartens.timetable.bahnApi.model.db.BahnStopEvent
import de.twomartens.timetable.bahnApi.model.db.BahnTimetable
import de.twomartens.timetable.model.common.RouteId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.types.HourAtDay
import de.twomartens.timetable.types.NonEmptyString
import org.mapstruct.*
import java.time.Clock
import java.time.ZonedDateTime

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
                userId: UserId, routeId: RouteId,
                hourAtDay: HourAtDay,
                clock: Clock): BahnTimetable {
        return BahnTimetable(
                userId,
                routeId,
                dto.eva,
                ZonedDateTime.of(hourAtDay.dateTime, clock.zone),
                NonEmptyString(dto.station),
                mapToDb(dto.stops, clock)
        )
    }

    fun mapToDb(stops: List<de.twomartens.timetable.bahnApi.model.dto.BahnStationStop>, clock: Clock): List<BahnStationStop> {
        return stops.map { mapToDB(it, clock) }
    }

    fun mapToDB(dto: de.twomartens.timetable.bahnApi.model.dto.BahnStationStop, clock: Clock): BahnStationStop {
        return BahnStationStop(
                dto.eva,
                dto.id,
                dto.tripLabel,
                mapToDB(dto.arrival, clock),
                mapToDB(dto.departure, clock)
        )
    }

    fun mapToDB(dto: de.twomartens.timetable.bahnApi.model.dto.BahnStopEvent?, clock: Clock): BahnStopEvent? {
        if (dto == null) {
            return null
        }

        return BahnStopEvent(
                dto.plannedDistantEndpoint,
                dto.changedDistantEndpoint,
                ZonedDateTime.of(dto.plannedTime, clock.zone),
                ZonedDateTime.of(dto.changedTime, clock.zone),
                dto.plannedPath,
                dto.changedPath,
                dto.plannedPlatform,
                dto.changedPlatform,
                dto.wings,
                dto.line
        )
    }
}