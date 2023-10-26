package de.twomartens.timetable.bahnApi.model.dto

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlAttribute

@XmlAccessorType(XmlAccessType.FIELD)
data class BahnTripLabel(
        @field:XmlAttribute(name = "c") var category: String,
        // S: S-Bahn, N: Nahverkehr, F: Fernverkehr
        @field:XmlAttribute(name = "f") var filterFlags: String,
        @field:XmlAttribute(name = "n") var trainNumber: String,
        @field:XmlAttribute(name = "o") var owner: String,
        @field:XmlAttribute(name = "t") var tripType: BahnTripType
) {
    constructor() : this("", "", "", "", BahnTripType.p)
}