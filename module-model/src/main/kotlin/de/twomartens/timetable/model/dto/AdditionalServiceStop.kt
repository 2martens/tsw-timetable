package de.twomartens.timetable.model.dto

import java.time.LocalTime

data class AdditionalServiceStop(
        val stop: Station,
        val arrivalTime: LocalTime,
        val departureTime: LocalTime,
        val platformAndSection: String,
        val loading: Boolean
)
