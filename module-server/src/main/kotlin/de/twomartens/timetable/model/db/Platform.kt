package de.twomartens.timetable.model.db

data class Platform(
        var name: String,
        var sections: MutableList<Section>
)
