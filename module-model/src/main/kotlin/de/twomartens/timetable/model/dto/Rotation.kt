package de.twomartens.timetable.model.dto

import java.time.LocalTime

data class Rotation(
        val id: String,
        val formationId: String,
        val firstServiceStartTime: LocalTime,
        val lastServiceEndTime: LocalTime,
        val startsInVault: Boolean
)
