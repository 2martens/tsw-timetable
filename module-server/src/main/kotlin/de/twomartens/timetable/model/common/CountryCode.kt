package de.twomartens.timetable.model.common

import de.twomartens.timetable.types.NonEmptyString

@JvmInline
value class CountryCode(val countryCode: NonEmptyString) {
    init {
        require(countryCode.value.length == 2) {
            "CountryCode must be only two characters long to conform to ISO 3166-1 alpha-2"
        }
    }
}
