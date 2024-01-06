package de.twomartens.timetable.model.dto

data class TswRoute(
        val id: String,
        val name: String,
        val country: Country,
        val stations: List<Station>,
        val firstStation: Station,
        val lastStation: Station,
        val numberOfStations: Int,
        val depots: List<Depot>,
        val portals: List<Portal>
)
