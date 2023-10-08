package de.twomartens.timetable.model

import java.time.LocalDateTime

@JvmInline
value class HourAtDay(val value: LocalDateTime) {
    init {
        require(value.minute == 0 && value.second == 0 && value.nano == 0) {
            "DateTime must only include an hour and the date."
        }
    }
}