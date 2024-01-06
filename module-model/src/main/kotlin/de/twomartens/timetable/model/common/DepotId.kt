package de.twomartens.timetable.model.common

import de.twomartens.timetable.types.NonEmptyString

@JvmInline
value class DepotId private constructor(val id: String) {
    companion object {
        fun of(id: NonEmptyString): DepotId {
            return DepotId(id.value)
        }
    }
}