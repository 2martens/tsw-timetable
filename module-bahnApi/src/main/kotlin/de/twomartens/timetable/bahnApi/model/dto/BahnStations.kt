package de.twomartens.timetable.bahnApi.model.dto

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "stations")
@XmlAccessorType(XmlAccessType.FIELD)
data class BahnStations(
        @field:XmlElement(name = "station") var stations: MutableList<BahnStation>
) {
    constructor() : this(mutableListOf())
}
