package de.twomartens.timetable.model.common

import de.twomartens.timetable.types.NonEmptyString

@JvmInline
value class RouteId private constructor(val id: String) {
    companion object {
        fun of(id: NonEmptyString): RouteId {
            return RouteId(id.value)
        }
    }
}