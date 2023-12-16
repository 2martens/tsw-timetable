package de.twomartens.timetable.model.dto

import de.twomartens.timetable.model.common.FormationId
import de.twomartens.timetable.model.common.Line
import de.twomartens.timetable.model.common.ServiceId

data class ServiceLinkingStage(
        val id: ServiceId,
        val formationId: FormationId,
        val line: Line,
        val direction: Direction,
        val formationReversed: Boolean,
        val startStop: ServiceStop,
        val fromDepotOrSidingAtStart: Boolean,
        val destinationStop: ServiceStop,
        val fromDepotOrSidingAtDestination: Boolean,
)

