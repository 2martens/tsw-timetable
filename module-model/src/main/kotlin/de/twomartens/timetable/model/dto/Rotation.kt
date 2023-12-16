package de.twomartens.timetable.model.dto

import de.twomartens.timetable.model.common.FormationId
import de.twomartens.timetable.model.common.RotationId
import java.time.LocalTime

data class Rotation(
        val id: RotationId,
        val formationId: FormationId,
        val firstServiceStartTime: LocalTime,
        val lastServiceEndTime: LocalTime,
        val startsInVault: Boolean
)
