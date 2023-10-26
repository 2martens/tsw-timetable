package de.twomartens.timetable.types

@JvmInline
value class NonEmptyString(val value: String) {
    init {
        require(value.isNotBlank()) {
            "Name must not be blank"
        }
    }
}