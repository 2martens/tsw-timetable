package de.twomartens.timetable.model

@JvmInline
value class NonEmptyString(val name: String) {
    init {
        require(name.isNotBlank()) {
            "Name must not be blank"
        }
    }
}