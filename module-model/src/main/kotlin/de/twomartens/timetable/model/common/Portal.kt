package de.twomartens.timetable.model.common

import de.twomartens.timetable.types.NonEmptyString
import java.time.Duration

data class Portal(
        var name: NonEmptyString,
        var nearestStationId: StationId,
        var formationTimes: Map<FormationId, Duration>
)
