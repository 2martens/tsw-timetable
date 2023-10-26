package de.twomartens.timetable.model.common

data class Platform(
        var name: String,
        var sections: MutableList<Section>
)
