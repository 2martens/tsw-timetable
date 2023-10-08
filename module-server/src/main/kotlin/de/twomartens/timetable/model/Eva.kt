package de.twomartens.timetable.model

@JvmInline
value class Eva(val value: Int) {
    init {
        require(value in 8000000..8999999) {
            "Eva number must start with an 8 and have 7 digits"
        }
    }
}
