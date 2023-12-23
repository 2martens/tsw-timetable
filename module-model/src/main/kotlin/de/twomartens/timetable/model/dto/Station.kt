package de.twomartens.timetable.model.dto

import de.twomartens.timetable.model.common.Platform
import de.twomartens.timetable.model.common.StationId
import de.twomartens.timetable.types.NonEmptyString

data class Station(
        val id: StationId,
        val name: NonEmptyString,
        val platforms: List<Platform>
)
