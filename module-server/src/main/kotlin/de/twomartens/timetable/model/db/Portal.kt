package de.twomartens.timetable.model.db

import de.twomartens.timetable.model.base.NonEmptyString
import kotlin.time.Duration

data class Portal(
        var name: NonEmptyString,
        var nearestStationId: StationId,
        var formationTimes: Map<FormationId, Duration>
)
