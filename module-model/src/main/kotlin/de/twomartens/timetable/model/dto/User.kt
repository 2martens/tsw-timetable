package de.twomartens.timetable.model.dto

import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.types.Email
import de.twomartens.timetable.types.NonEmptyString

data class User(
        val id: UserId,
        val name: NonEmptyString,
        val email: Email
)
