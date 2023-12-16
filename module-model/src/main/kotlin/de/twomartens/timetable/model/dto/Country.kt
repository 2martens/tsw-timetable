package de.twomartens.timetable.model.dto

import de.twomartens.timetable.model.common.CountryCode
import de.twomartens.timetable.types.NonEmptyString

data class Country(
        val countryCode: CountryCode,
        val name: NonEmptyString
)
