package de.twomartens.timetable.bahnApi.model.dto

import de.twomartens.timetable.bahnApi.jaxb.LocalDateTimeAdapter
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlAttribute
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter
import java.time.LocalDateTime

@XmlAccessorType(XmlAccessType.FIELD)
data class BahnStopEvent(
    @field:XmlAttribute(name = "pde") var plannedDistantEndpoint: String,
    @field:XmlAttribute(name = "cde") var changedDistantEndpoint: String,
    @field:XmlAttribute(name = "pt")
    @field:XmlJavaTypeAdapter(value = LocalDateTimeAdapter::class)
    var plannedTime: LocalDateTime?,
    @field:XmlAttribute(name = "ct")
    @field:XmlJavaTypeAdapter(value = LocalDateTimeAdapter::class)
    var changedTime: LocalDateTime?,
    @field:XmlAttribute(name = "ppth") var plannedPath: String,
    @field:XmlAttribute(name = "cpth") var changedPath: String,
    @field:XmlAttribute(name = "pp") var plannedPlatform: String,
    @field:XmlAttribute(name = "cp") var changedPlatform: String,
    @field:XmlAttribute(name = "wings") var wings: String,
    @field:XmlAttribute(name = "l") var line: String
) {
    constructor(): this("", "",
            null,
            null,
            "", "", "", "",
            "", "")
}