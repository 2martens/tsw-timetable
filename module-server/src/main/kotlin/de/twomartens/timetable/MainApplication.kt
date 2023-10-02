package de.twomartens.timetable

import de.twomartens.timetable.bahnApi.service.BahnApiService
import de.twomartens.timetable.bahnApi.service.BahnDatabaseService
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
        private val bahnApiService: BahnApiService,
        private val bahnDatabaseService: BahnDatabaseService
) {
    @EventListener(ApplicationReadyEvent::class)
    fun ready() {
        val timetable = bahnApiService.fetchTimetable(8000207,
                LocalDate.of(2023, Month.OCTOBER, 1),
                LocalTime.of(3, 0))
        log.info { "bs" }
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}

fun main(args: Array<String>) {
    runApplication<MainApplication>(*args)
}

