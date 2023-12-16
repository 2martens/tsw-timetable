package de.twomartens.timetable.model.common

import de.twomartens.timetable.types.PositiveInteger

@JvmInline
value class TrackId private constructor(val value: Int) {
    companion object {
        fun of(id: PositiveInteger): TrackId {
            return TrackId(id.value)
        }
    }
}
