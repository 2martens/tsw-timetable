package de.twomartens.timetable.route

import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.model.dto.Depot
import de.twomartens.timetable.model.dto.Portal
import de.twomartens.timetable.model.dto.TravelDuration
import de.twomartens.timetable.model.dto.TswRoute
import de.twomartens.timetable.types.ZeroOrPositiveInteger
import org.mapstruct.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface RouteMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    fun mapToDB(userId: UserId, dto: TswRoute): de.twomartens.timetable.model.db.TswRoute {
        return de.twomartens.timetable.model.db.TswRoute(
                userId,
                dto.id,
                dto.name,
                dto.country,
                dto.stations,
                mapDepotsToDB(dto.depots),
                mapPortalsToDB(dto.portals)
        )
    }

    fun mapDepotsToDB(depots: List<Depot>): List<de.twomartens.timetable.model.db.Depot> {
        return depots.map {
            de.twomartens.timetable.model.db.Depot(
                    it.id,
                    it.name,
                    it.nearestStation,
                    it.tracks,
                    mapTravelDurationsToDB(it.travelDurations)
            )
        }
    }

    fun mapPortalsToDB(portals: List<Portal>): List<de.twomartens.timetable.model.db.Portal> {
        return portals.map {
            de.twomartens.timetable.model.db.Portal(
                    it.id,
                    it.name,
                    it.nearestStation,
                    mapTravelDurationsToDB(it.travelDurations)
            )
        }
    }

    fun mapTravelDurationsToDB(travelDurations: List<TravelDuration>): List<de.twomartens.timetable.model.db.TravelDuration> {
        return travelDurations.map {
            de.twomartens.timetable.model.db.TravelDuration(
                    it.formation,
                    it.time.toLong(DurationUnit.MILLISECONDS)
            )
        }
    }

    fun mapRoutesToDto(routes: List<de.twomartens.timetable.model.db.TswRoute>): List<TswRoute>

    fun mapToDto(db: de.twomartens.timetable.model.db.TswRoute): TswRoute {
        return TswRoute(
                db.routeId,
                db.name,
                db.country,
                db.stations,
                db.stations.first(),
                db.stations.last(),
                ZeroOrPositiveInteger(db.stations.size),
                mapDepotsToDto(db.depots),
                mapPortalsToDto(db.portals)
        )
    }

    fun mapDepotsToDto(depots: List<de.twomartens.timetable.model.db.Depot>): List<Depot> {
        return depots.map { depot ->
            Depot(
                    depot.id,
                    depot.name,
                    depot.nearestStation,
                    depot.tracks,
                    mapTravelDurationsToDto(depot.travelDurations)
            )
        }
    }

    fun mapPortalsToDto(portals: List<de.twomartens.timetable.model.db.Portal>): List<Portal> {
        return portals.map { portal ->
            Portal(
                    portal.id,
                    portal.name,
                    portal.nearestStation,
                    mapTravelDurationsToDto(portal.travelDurations)
            )
        }
    }

    fun mapTravelDurationsToDto(travelDurations: List<de.twomartens.timetable.model.db.TravelDuration>): List<TravelDuration> {
        return travelDurations.map {
            TravelDuration(
                    it.formation,
                    it.time.toDuration(DurationUnit.MILLISECONDS)
            )
        }
    }
}