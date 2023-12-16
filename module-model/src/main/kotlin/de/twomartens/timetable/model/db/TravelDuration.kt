package de.twomartens.timetable.model.db

import de.twomartens.timetable.model.dto.Formation

data class TravelDuration(
        val formation: Formation,
        val time: Long
)
