package de.twomartens.timetable.model.dto

import de.twomartens.timetable.model.common.FormationId
import de.twomartens.timetable.types.NonEmptyString

data class Formation(
        val id: FormationId,
        val name: NonEmptyString,
        val trainSimWorldFormationId: FormationId?,
        val coaches: List<NonEmptyString>
)
