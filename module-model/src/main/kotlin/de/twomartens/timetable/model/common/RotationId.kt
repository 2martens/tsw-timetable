package de.twomartens.timetable.model.common

import de.twomartens.timetable.types.PositiveInteger

@JvmInline
value class RotationId private constructor(val id: Int) {
    companion object {
        fun of(id: PositiveInteger): RotationId {
            return RotationId(id.value)
        }
    }
}
