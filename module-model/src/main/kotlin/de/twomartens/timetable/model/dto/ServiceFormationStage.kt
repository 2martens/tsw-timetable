package de.twomartens.timetable.model.dto

data class ServiceFormationStage(
        val id: String,
        val line: String,
        val direction: Direction,
        val formation: String,
        val formationReversed: Boolean,
        val startStop: ServiceStop,
        val destinationStop: ServiceStop
)
