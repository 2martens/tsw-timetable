package de.twomartens.timetable.model.dto

import de.twomartens.timetable.model.common.Line
import de.twomartens.timetable.model.common.ServiceId

data class ServiceRotationStage(
        val id: ServiceId,
        val line: Line,
        val originStop: String,
        val virtualDestinations: List<String>,
        val startingFrom: String,
        val endingIn: String,
        val serviceStartTime: String,
        val stops: List<ServiceStop>
)
