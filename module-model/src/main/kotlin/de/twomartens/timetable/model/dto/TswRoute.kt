package de.twomartens.timetable.model.dto

import de.twomartens.timetable.model.common.RouteId
import de.twomartens.timetable.types.NonEmptyString
import de.twomartens.timetable.types.ZeroOrPositiveInteger

data class TswRoute(
        val id: RouteId,
        val name: NonEmptyString,
        val country: Country,
        val stations: List<Station>,
        val firstStation: Station,
        val lastStation: Station,
        val numberOfStations: ZeroOrPositiveInteger,
        val depots: List<Depot>,
        val portals: List<Portal>
)
