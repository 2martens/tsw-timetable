package de.twomartens.timetable.types

@JvmInline
value class Hour private constructor(val value: Int) {
    init {
        require(value in 0..23) {
            "Hour must be between 0 and 23"
        }
    }

    companion object {
        fun of(hour: Int): Hour {
            return Hour(hour)
        }
    }
}