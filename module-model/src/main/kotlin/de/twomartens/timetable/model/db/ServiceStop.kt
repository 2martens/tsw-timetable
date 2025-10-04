package de.twomartens.timetable.model.db

import java.time.ZonedDateTime

data class ServiceStop(
        /** The EVA number of the station */
        val eva: String,

        /** Planned arrival time at this stop */
        var plannedArrivalTime: ZonedDateTime? = null,

        /** Planned departure time from this stop */
        var plannedDepartureTime: ZonedDateTime? = null,

        /** Platform for arrival */
        var plannedArrivalPlatform: String? = null,

        /** Platform for departure */
        var plannedDeparturePlatform: String? = null,

        /** Line information for arrival */
        var arrivalLine: String? = null,

        /** Line information for departure */
        var departureLine: String? = null,

        /** Path information for arrival */
        var plannedArrivalPath: String? = null,

        /** Path information for departure */
        var plannedDeparturePath: String? = null
)
