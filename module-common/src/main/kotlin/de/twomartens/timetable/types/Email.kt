package de.twomartens.timetable.types

@JvmInline
value class Email private constructor(val value: String) {
    companion object {
        fun of(email: NonEmptyString): Email {
            require(email.value.contains("@")) { "Invalid email format" }
            return Email(email.value)
        }
    }
}