package de.twomartens.timetable.bahnApi.model.dto

import de.twomartens.timetable.model.Eva
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlAttribute

@XmlAccessorType(XmlAccessType.FIELD)
data class BahnStation(
        @field:XmlAttribute
    var name: String,
        @field:XmlAttribute
    var eva: Eva?,
        @field:XmlAttribute
    var ds100: String,
        @field:XmlAttribute
    var db: Boolean
) {
    constructor(): this("", null, "", true)
}