package de.twomartens.timetable.types

@JvmInline
value class ZeroOrPositiveInteger(val value: Int) {
    init {
        require(value >= 0) {
            "Value must be zero or positive integer"
        }
    }
}
