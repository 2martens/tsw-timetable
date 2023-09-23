package de.twomartens.timetable.bahnApi.jaxb

import jakarta.xml.bind.annotation.adapters.XmlAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class LocalDateTimeAdapter : XmlAdapter<String?, LocalDateTime>() {

    @Throws(Exception::class)
    override fun unmarshal(xmlValue: String?): LocalDateTime {
        return LocalDateTime.parse(xmlValue, dateFormatter.get())
    }

    @Throws(Exception::class)
    override fun marshal(kotlinValue: LocalDateTime): String? {
        return dateFormatter.get().format(kotlinValue)
    }

    companion object {
        private val dateFormatter: ThreadLocal<DateTimeFormatter> = object : ThreadLocal<DateTimeFormatter>() {
            override fun initialValue(): DateTimeFormatter {
                return DateTimeFormatter.ofPattern("yyMMddHHmm", Locale.GERMANY)
            }
        }
    }

}