package de.twomartens.timetable.model.db

import de.twomartens.timetable.types.NonEmptyString
import kotlin.time.Duration

data class Depot(
        var name: NonEmptyString,
        var nearestStationId: StationId,
        var tracks: Map<TrackId, CoachCapacity>,
        var formationTimes: Map<FormationId, Duration>
)
