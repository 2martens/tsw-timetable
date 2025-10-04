package de.twomartens.timetable.bahnApi.model.db

import java.time.ZonedDateTime

data class BahnStopEvent(
        var plannedDistantEndpoint: String,
        var changedDistantEndpoint: String,
        var plannedTime: ZonedDateTime?,
        var changedTime: ZonedDateTime?,
        var plannedPath: String,
        var changedPath: String,
        var plannedPlatform: String,
        var changedPlatform: String,
        var wings: String,
        var line: String
)
