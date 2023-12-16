package de.twomartens.timetable.model.dto

import de.twomartens.timetable.model.common.FormationId
import de.twomartens.timetable.model.common.Line
import de.twomartens.timetable.model.common.ServiceId

data class ServiceFormationStage(
        val id: ServiceId,
        val line: Line,
        val direction: Direction,
        val formation: FormationId,
        val formationReversed: Boolean,
        val startStop: ServiceStop,
        val destinationStop: ServiceStop
)
