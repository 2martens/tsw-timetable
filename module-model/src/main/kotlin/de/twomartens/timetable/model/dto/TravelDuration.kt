package de.twomartens.timetable.model.dto

import kotlin.time.Duration

data class TravelDuration(
        val formation: Formation,
        val time: Duration
)
