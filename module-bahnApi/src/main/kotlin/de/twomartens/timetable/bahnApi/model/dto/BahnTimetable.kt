package de.twomartens.timetable.bahnApi.model.dto

import de.twomartens.timetable.bahnApi.model.Eva
import jakarta.xml.bind.annotation.*

@XmlRootElement(name = "timetable")
@XmlAccessorType(XmlAccessType.FIELD)
data class BahnTimetable(
        @field:XmlAttribute var eva: Eva,
        @field:XmlAttribute var station: String,
        @field:XmlElement(name = "s") var stops: List<BahnStationStop>
) {
    constructor() : this(Eva.UNKNOWN, "", listOf())
}