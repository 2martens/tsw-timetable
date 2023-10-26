package de.twomartens.timetable.bahnApi.model

import java.time.LocalDate

data class FetchDates(val previousDay: LocalDate, val fetchDate: LocalDate, val nextDate: LocalDate)
