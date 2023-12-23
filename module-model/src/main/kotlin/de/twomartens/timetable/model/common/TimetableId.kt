package de.twomartens.timetable.model.common

import de.twomartens.timetable.types.NonEmptyString

@JvmInline
value class TimetableId private constructor(val value: String) {
    companion object {
        fun of(id: NonEmptyString): TimetableId {
            return TimetableId(id.value)
        }
    }
}