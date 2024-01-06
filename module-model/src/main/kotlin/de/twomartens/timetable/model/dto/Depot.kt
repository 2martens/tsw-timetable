package de.twomartens.timetable.model.dto

data class Depot(
        val id: String,
        val name: String,
        val nearestStation: Station,
        val tracks: List<Track>,
        val travelDurations: List<TravelDuration>
)
