package de.twomartens.timetable.model.dto

import de.twomartens.timetable.model.common.FormationId
import de.twomartens.timetable.model.common.Line
import de.twomartens.timetable.model.common.ServiceId
import java.time.LocalTime

data class AdditionalService(
        val id: ServiceId,
        val line: Line,
        val formationId: FormationId,
        val direction: Direction,
        val start: Station,
        val destination: Station,
        val serviceStartTime: LocalTime,
        val stops: List<AdditionalServiceStop>
)
