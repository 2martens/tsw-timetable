package de.twomartens.timetable.bahnApi.model.dto

import de.twomartens.timetable.bahnApi.model.Eva
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlAttribute
import jakarta.xml.bind.annotation.XmlElement

@XmlAccessorType(XmlAccessType.FIELD)
data class BahnStationStop(
        @field:XmlAttribute var eva: Eva,
        @field:XmlAttribute var id: String,
        @field:XmlElement(name = "tl") var tripLabel: BahnTripLabel,
        @field:XmlElement(name = "ar") var arrival: BahnStopEvent?,
        @field:XmlElement(name = "dp") var departure: BahnStopEvent?
) {
    constructor() : this(Eva.UNKNOWN, "", BahnTripLabel(), null, null)
}