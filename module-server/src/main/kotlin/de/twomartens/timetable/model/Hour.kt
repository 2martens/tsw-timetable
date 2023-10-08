package de.twomartens.timetable.model

@JvmInline
value class Hour(val value: Int) {
    init {
        require(value in 0..23) {
            "Hour must be between 0 and 23"
        }
    }
}