package de.twomartens.timetable.model.db

import de.twomartens.timetable.types.NonEmptyString

@JvmInline
value class CountryCode(val countryCode: NonEmptyString) {
    init {
        require(countryCode.name.length == 2) {
            "CountryCode must be only two characters long to conform to ISO 3166-1 alpha-2"
        }
    }
}
