package de.twomartens.timetable.model.dto

import de.twomartens.timetable.model.common.CoachCapacity
import de.twomartens.timetable.types.NonEmptyString
import de.twomartens.timetable.types.ZeroOrPositiveInteger

data class Track(
        val id: ZeroOrPositiveInteger,
        val name: NonEmptyString,
        val capacity: CoachCapacity
)
