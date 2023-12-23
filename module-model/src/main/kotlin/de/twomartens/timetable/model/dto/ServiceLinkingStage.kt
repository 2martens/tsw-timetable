package de.twomartens.timetable.model.dto

data class ServiceLinkingStage(
        val id: String,
        val formationId: String,
        val line: String,
        val direction: Direction,
        val formationReversed: Boolean,
        val startStop: ServiceStop,
        val fromDepotOrSidingAtStart: Boolean,
        val destinationStop: ServiceStop,
        val fromDepotOrSidingAtDestination: Boolean,
)

