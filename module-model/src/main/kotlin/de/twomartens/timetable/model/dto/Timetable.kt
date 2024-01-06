package de.twomartens.timetable.model.dto

import java.time.LocalDate

data class Timetable(
        val id: String,
        val name: String,
        val routeId: String,
        val routeName: String,
        val date: LocalDate,
        val state: TimetableState,
        val numberOfServices: Int
)
