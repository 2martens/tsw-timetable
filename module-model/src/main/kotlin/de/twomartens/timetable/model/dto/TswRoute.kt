package de.twomartens.timetable.model.dto

import de.twomartens.timetable.model.common.Depot
import de.twomartens.timetable.model.common.Portal
import de.twomartens.timetable.types.NonEmptyString

data class TswRoute(
        val name: NonEmptyString,
        val country: Country,
        val stations: List<Station>,
        val portals: List<Portal>,
        val depots: List<Depot>
)
