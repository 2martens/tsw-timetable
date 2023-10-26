package de.twomartens.timetable.model.common

import de.twomartens.timetable.types.NonEmptyString

@JvmInline
value class StationId private constructor(val value: String) {

    companion object {
        private val idPattern = Regex("^\\w{2}-(?<countryStationId>\\w.*)")

        fun of(id: NonEmptyString): StationId {
            require(idPattern.matches(id.value)) {
                "StationId must start with two character country code, followed by a dash, " +
                        "and at least one word character"
            }

            return StationId(id.value)
        }
    }

    val stationIdWithinCountry: String
        get() {
            val matchResult = idPattern.matchEntire(value)
            val groupMatch = matchResult?.groups?.get("countryStationId")?.value

            require(matchResult != null && groupMatch != null)

            return groupMatch
        }
}
