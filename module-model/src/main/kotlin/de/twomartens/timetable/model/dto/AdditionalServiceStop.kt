package de.twomartens.timetable.model.dto

import de.twomartens.timetable.types.NonEmptyString
import java.time.LocalTime

data class AdditionalServiceStop(
        val stop: Station,
        val arrivalTime: LocalTime,
        val departureTime: LocalTime,
        val platformAndSection: NonEmptyString,
        val loading: Boolean
)
