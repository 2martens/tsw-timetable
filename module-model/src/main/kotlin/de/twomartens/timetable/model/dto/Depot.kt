package de.twomartens.timetable.model.dto

import de.twomartens.timetable.model.common.DepotId
import de.twomartens.timetable.types.NonEmptyString

data class Depot(
        val id: DepotId,
        val name: NonEmptyString,
        val nearestStation: Station,
        val tracks: List<Track>,
        val travelDurations: List<TravelDuration>
)
