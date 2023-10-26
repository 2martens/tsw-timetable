package de.twomartens.timetable.model.common

import de.twomartens.timetable.types.NonEmptyString

@JvmInline
value class Section private constructor(val value: String) {
    companion object {
        fun of(section: NonEmptyString): Section {
            return Section(section.value)
        }
    }
}
