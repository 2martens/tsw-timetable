package de.twomartens.timetable.model.dto

import de.twomartens.timetable.model.common.FormationId
import de.twomartens.timetable.types.NonEmptyString
import de.twomartens.timetable.types.ZeroOrPositiveInteger

data class Formation(
        val id: FormationId,
        val name: NonEmptyString,
        val trainSimWorldFormation: Formation?,
        val formation: String,
        val length: ZeroOrPositiveInteger
)
