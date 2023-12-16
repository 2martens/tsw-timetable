package de.twomartens.timetable.model.dto

import de.twomartens.timetable.types.NonEmptyString
import java.time.LocalDate

data class Timetable(
        val name: NonEmptyString,
        val country: Country,
        val routeName: NonEmptyString,
        val fetchDate: LocalDate,
        val state: TimetableState,
        val numberOfServices: String
)
