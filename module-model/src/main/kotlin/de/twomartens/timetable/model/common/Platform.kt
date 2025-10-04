package de.twomartens.timetable.model.common

data class Platform(
        var name: String,
) {
    private var sectionsInternal: MutableSet<Section> = mutableSetOf()

    val sections: Set<Section> get() = sectionsInternal

    fun addSection(section: Section) {
        sectionsInternal.add(section)
    }

    fun addAllSections(sections: Collection<Section>) {
        sectionsInternal.addAll(sections)
    }
}
