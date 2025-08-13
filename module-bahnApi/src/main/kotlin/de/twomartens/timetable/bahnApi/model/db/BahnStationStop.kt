package de.twomartens.timetable.bahnApi.model.db

import de.twomartens.timetable.bahnApi.model.Eva
import de.twomartens.timetable.bahnApi.model.dto.BahnTripLabel

data class BahnStationStop(
        var eva: Eva,
        var id: String,
        var tripLabel: BahnTripLabel,
        var arrival: BahnStopEvent?,
        var departure: BahnStopEvent?
)