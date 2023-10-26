package de.twomartens.timetable.model.common

import de.twomartens.timetable.types.NonEmptyString

@JvmInline
value class UserId private constructor(val value: String) {
    companion object {
        fun of(id: NonEmptyString): UserId {
            return UserId(id.value)
        }
    }
}
