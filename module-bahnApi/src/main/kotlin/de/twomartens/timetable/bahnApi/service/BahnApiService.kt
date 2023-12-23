package de.twomartens.timetable.bahnApi.service

import de.twomartens.timetable.bahnApi.model.Eva
import de.twomartens.timetable.bahnApi.model.dto.BahnStation
import de.twomartens.timetable.bahnApi.model.dto.BahnStations
import de.twomartens.timetable.bahnApi.model.dto.BahnTimetable
import de.twomartens.timetable.bahnApi.property.BahnApiProperties
import de.twomartens.timetable.types.HourAtDay
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Service
class BahnApiService(
        private val restClient: RestClient,
        private val properties: BahnApiProperties
) {
    fun fetchStations(pattern: String): List<BahnStation> {
        val body = restClient.get()
                .uri("https://apis.deutschebahn.com/db-api-marketplace/apis/timetables/v1/station/${pattern}")
                .headers {
                    it.accept = mutableListOf(MediaType.APPLICATION_XML)
                    it.contentType = MediaType.APPLICATION_XML
                    it.set("DB-Client-Id", properties.clientId)
                    it.set("DB-Api-Key", properties.clientSecret)
                }
                .retrieve()
                .body(BahnStations::class.java)
        return body?.stations ?: listOf()
    }

    fun fetchTimetable(eva: Eva, hourAtDay: HourAtDay): BahnTimetable {
        val dateFormatter = DateTimeFormatter.ofPattern("yyMMdd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH")
        val time = LocalTime.of(hourAtDay.hour.value, 0)
        val body = restClient.get()
                .uri("https://apis.deutschebahn.com/db-api-marketplace/apis/timetables/v1/plan/" +
                        "${eva}/${hourAtDay.date.format(dateFormatter)}/${time.format(timeFormatter)}")
                .headers {
                    it.accept = mutableListOf(MediaType.APPLICATION_XML)
                    it.contentType = MediaType.APPLICATION_XML
                    it.set("DB-Client-Id", properties.clientId)
                    it.set("DB-Api-Key", properties.clientSecret)
                }
                .retrieve()
                .body(BahnTimetable::class.java)
        return body!!
    }
}