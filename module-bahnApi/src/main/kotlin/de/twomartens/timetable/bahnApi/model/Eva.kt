package de.twomartens.timetable.bahnApi.model

@JvmInline
value class Eva(val value: Int) {
    init {
        if (isKnown()) {
            require(value in 8000000..8999999) {
                "Eva number must start with an 8 and have 7 digits"
            }
        }
    }

    private fun isKnown() = value != -1

    companion object {
        val UNKNOWN = Eva(-1)

        fun of(stationId: String): Eva {
            return Eva(stationId.toInt())
        }
    }
}
