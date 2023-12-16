package de.twomartens.timetable.model.common

import de.twomartens.timetable.types.NonEmptyString
import java.time.Duration

data class Depot(
        var name: NonEmptyString,
        var nearestStationId: StationId,
        var tracks: Map<TrackId, CoachCapacity>,
        var formationTimes: Map<FormationId, Duration>
)
