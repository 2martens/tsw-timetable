package de.twomartens.timetable.model.common

import de.twomartens.timetable.types.NonEmptyString

@JvmInline
value class ServiceId private constructor(val value: String) {
    companion object {
        fun of(id: NonEmptyString): ServiceId {
            return ServiceId(id.value)
        }
    }
}
