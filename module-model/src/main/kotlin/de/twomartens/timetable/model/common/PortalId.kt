package de.twomartens.timetable.model.common

import de.twomartens.timetable.types.NonEmptyString

@JvmInline
value class PortalId private constructor(val id: String) {
    companion object {
        fun of(id: NonEmptyString): PortalId {
            return PortalId(id.value)
        }
    }
}