package de.twomartens.timetable.bahnApi.model.dto

import jakarta.xml.bind.annotation.*

@XmlRootElement(name = "timetable")
@XmlAccessorType(XmlAccessType.FIELD)
data class BahnTimetable(
    @field:XmlAttribute var eva: Int?,
    @field:XmlAttribute var station: String,
    @field:XmlElement(name = "s") var stops: List<BahnStationStop>
) {
    constructor(): this(null, "", mutableListOf())
}