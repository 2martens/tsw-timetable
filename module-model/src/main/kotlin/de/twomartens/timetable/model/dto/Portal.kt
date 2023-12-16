package de.twomartens.timetable.model.dto

import de.twomartens.timetable.model.common.PortalId
import de.twomartens.timetable.types.NonEmptyString

data class Portal(
        val id: PortalId,
        val name: NonEmptyString,
        val nearestStation: Station,
        val travelDurations: List<TravelDuration>
)
