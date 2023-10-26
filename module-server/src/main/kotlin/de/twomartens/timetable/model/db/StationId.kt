package de.twomartens.timetable.model.db

import de.twomartens.timetable.types.NonEmptyString

@JvmInline
value class StationId(val id: NonEmptyString) {
    init {
        require(idPattern.matches(id.name)) {
            "StationId must start with two character country code, followed by a dash, " +
                    "and at least one word character"
        }
    }

    companion object {
        private val idPattern = Regex("^\\w{2}-(?<countryStationId>\\w.*)")
    }

    val stationIdWithinCountry: NonEmptyString
        get() {
            val matchResult = idPattern.matchEntire(id.name)
            val groupMatch = matchResult?.groups?.get("countryStationId")?.value
            return when {
                matchResult == null || groupMatch == null ->
                    throw IllegalArgumentException("id wasn't matched by pattern")

                else -> NonEmptyString(groupMatch)
            }
        }
}
