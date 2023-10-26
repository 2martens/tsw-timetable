package de.twomartens.timetable.model.dto

import de.twomartens.timetable.types.NonEmptyString

data class ServiceStop(
        val stop: Station,
        val arrivalTime: String,
        val departureTime: String,
        val platform: NonEmptyString,
        val section: String
)
