package de.twomartens.timetable.model.dto

import java.time.LocalTime

data class AdditionalService(
        val id: String,
        val line: String,
        val formationId: String,
        val direction: Direction,
        val start: Station,
        val destination: Station,
        val serviceStartTime: LocalTime,
        val stops: List<AdditionalServiceStop>
)
