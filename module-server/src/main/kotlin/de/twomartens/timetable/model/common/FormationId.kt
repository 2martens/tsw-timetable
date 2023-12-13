package de.twomartens.timetable.model.common

import de.twomartens.timetable.types.NonEmptyString

@JvmInline
value class FormationId private constructor(val id: String) {
    companion object {
        fun of(id: NonEmptyString): FormationId {
            return FormationId(id.value)
        }
    }
}