package de.twomartens.timetable.model.base

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@JvmInline
value class HourAtDay private constructor(private val value: LocalDateTime) {
    init {
        require(value.minute == 0 && value.second == 0 && value.nano == 0) {
            "DateTime must only include an hour and the date."
        }
    }

    val date: LocalDate
        get() {
            return value.toLocalDate()
        }

    val hour: Hour
        get() {
            return Hour.of(value.hour)
        }

    val dateTime: LocalDateTime
        get() {
            return value
        }

    companion object {
        fun of(dateTime: LocalDateTime): HourAtDay {
            return HourAtDay(dateTime.truncatedTo(ChronoUnit.HOURS))
        }

        fun of(hour: Hour, date: LocalDate): HourAtDay {
            return HourAtDay(date.atTime(hour.value, 0))
        }
    }
}