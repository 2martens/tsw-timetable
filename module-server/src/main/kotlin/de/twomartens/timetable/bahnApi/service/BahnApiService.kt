package de.twomartens.timetable.bahnApi.service

import de.twomartens.timetable.bahnApi.model.Eva
import de.twomartens.timetable.bahnApi.model.dto.BahnStation
import de.twomartens.timetable.bahnApi.model.dto.BahnStations
import de.twomartens.timetable.bahnApi.model.dto.BahnTimetable
import de.twomartens.timetable.bahnApi.property.BahnApiProperties
import de.twomartens.timetable.model.base.HourAtDay
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Service
class BahnApiService(
        private val restTemplate: RestTemplate,
        private val properties: BahnApiProperties
) {
    fun fetchStations(pattern: String): List<BahnStation> {
        val requestEntity = buildRequestEntity<BahnStations>()
        val response = restTemplate.exchange(
        "https://apis.deutschebahn.com/db-api-marketplace/apis/timetables/v1/station/${pattern}",
                HttpMethod.GET,
                requestEntity,
                BahnStations::class.java
        )
        val body = response.body
        return body?.stations ?: listOf()
    }

    fun fetchTimetable(eva: Eva, hourAtDay: HourAtDay): BahnTimetable {
        val requestEntity = buildRequestEntity<BahnTimetable>()
        val dateFormatter = DateTimeFormatter.ofPattern("yyMMdd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH")
        val time = LocalTime.of(hourAtDay.hour.value, 0)
        val response = restTemplate.exchange(
                "https://apis.deutschebahn.com/db-api-marketplace/apis/timetables/v1/plan/" +
                        "${eva}/${hourAtDay.date.format(dateFormatter)}/${time.format(timeFormatter)}",
                HttpMethod.GET,
                requestEntity,
                BahnTimetable::class.java
        )
        return response.body!!
    }

    private fun <T> buildRequestEntity(): HttpEntity<T> {
        val headers = HttpHeaders()
        headers.accept = mutableListOf(MediaType.APPLICATION_XML)
        headers.contentType = MediaType.APPLICATION_XML
        headers.set("DB-Client-Id", properties.clientId)
        headers.set("DB-Api-Key", properties.clientSecret)
        return HttpEntity<T>(headers)
    }
}