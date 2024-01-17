package de.twomartens.timetable.bahnApi.service

import de.twomartens.timetable.bahnApi.model.Eva
import de.twomartens.timetable.bahnApi.model.dto.BahnStation
import de.twomartens.timetable.bahnApi.model.dto.BahnStations
import de.twomartens.timetable.bahnApi.model.dto.BahnTimetable
import de.twomartens.timetable.bahnApi.property.BahnApiProperties
import de.twomartens.timetable.types.HourAtDay
import mu.KotlinLogging
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
        val day = hourAtDay.date.format(dateFormatter)
        val hour = time.format(timeFormatter)
        val bahnErrorHandler = BahnErrorHandler()
        val response = restClient.get()
                .uri("https://apis.deutschebahn.com/db-api-marketplace/apis/timetables/v1/plan/" +
                        "${eva.value}/${day}/${hour}")
                .headers {
                    it.accept = mutableListOf(MediaType.APPLICATION_XML)
                    it.contentType = MediaType.APPLICATION_XML
                    it.set("DB-Client-Id", properties.clientId)
                    it.set("DB-Api-Key", properties.clientSecret)
                }
                .retrieve()
                .onStatus(bahnErrorHandler)
        log.debug { "Response from DB API, station [${eva.value}], date [$day], hour [$hour]" }
        val body = response.body(BahnTimetable::class.java)
        body!!.eva = eva
        return body
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}