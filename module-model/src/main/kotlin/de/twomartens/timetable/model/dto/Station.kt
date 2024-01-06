package de.twomartens.timetable.model.dto

import de.twomartens.timetable.model.common.Platform

data class Station(
        val id: String,
        val name: String,
        val platforms: List<Platform>
)
