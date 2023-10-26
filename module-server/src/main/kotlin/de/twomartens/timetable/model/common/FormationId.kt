package de.twomartens.timetable.model.common

import de.twomartens.timetable.types.PositiveInteger

@JvmInline
value class FormationId private constructor(val id: Int) {
    companion object {
        val EMPTY = FormationId(-1)

        fun of(id: PositiveInteger): FormationId {
            return FormationId(id.value)
        }
    }
}