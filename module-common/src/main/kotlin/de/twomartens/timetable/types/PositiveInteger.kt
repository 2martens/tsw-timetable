package de.twomartens.timetable.types

@JvmInline
value class PositiveInteger(val value: Int) {
    init {
        require(value > 0) {
            "Value must be positive integer"
        }
    }
}
