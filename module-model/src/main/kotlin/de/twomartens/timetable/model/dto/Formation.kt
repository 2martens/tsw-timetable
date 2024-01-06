package de.twomartens.timetable.model.dto

data class Formation(
        val id: String,
        val name: String,
        val trainSimWorldFormation: Formation?,
        val formation: String,
        val length: Int
)
