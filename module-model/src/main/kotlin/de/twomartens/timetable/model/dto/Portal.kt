package de.twomartens.timetable.model.dto

data class Portal(
        val id: String,
        val name: String,
        val nearestStation: Station,
        val travelDurations: List<TravelDuration>
)
