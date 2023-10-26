package de.twomartens.timetable.model.dto

import de.twomartens.timetable.model.common.Line
import de.twomartens.timetable.model.common.ServiceId
import de.twomartens.timetable.types.NonEmptyString

data class ServiceRotationStage(
        val id: ServiceId,
        val line: Line,
        val originStop: NonEmptyString,
        val virtualDestinations: List<NonEmptyString>,
        val startingFrom: NonEmptyString,
        val endingIn: NonEmptyString,
        val serviceStartTime: NonEmptyString,
        val stops: List<ServiceStop>
)
