package de.twomartens.timetable.model.dto

data class ServiceStop(
        val stop: Station,
        val arrivalTime: String,
        val departureTime: String,
        val platform: String,
        val section: String
)
