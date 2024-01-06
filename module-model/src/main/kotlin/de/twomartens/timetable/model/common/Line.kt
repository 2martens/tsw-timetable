package de.twomartens.timetable.model.common

import de.twomartens.timetable.types.NonEmptyString

@JvmInline
value class Line private constructor(val name: String) {
    companion object {
        val EMPTY = Line("EMPTY")

        fun of(name: NonEmptyString): Line {
            return Line(name.value)
        }
    }
}