package de.twomartens.timetable.model.base

@JvmInline
value class PositiveInteger(val value: Int) {
    init {
        require(value > 0) {
            "Value must be positive integer"
        }
    }
}
