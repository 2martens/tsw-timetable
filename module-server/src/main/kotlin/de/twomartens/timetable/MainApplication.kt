package de.twomartens.timetable

import de.twomartens.timetable.bahnApi.service.BahnApiService
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.scheduling.annotation.EnableScheduling
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month

@EnableMongoAuditing
@EnableScheduling
@SpringBootApplication
open class MainApplication(
        private val bahnApiService: BahnApiService
) {
    @EventListener(ApplicationReadyEvent::class)
    fun ready() {
        val result = bahnApiService.fetchStations("Köln Hbf")
        val koeln = result[0]
        val timetable = bahnApiService.fetchTimetable(koeln.eva,
                LocalDate.of(2023, Month.SEPTEMBER, 23),
                LocalTime.of(17, 0))
        log.info("stations: $result")
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}

fun main(args: Array<String>) {
    runApplication<MainApplication>(*args)
}

