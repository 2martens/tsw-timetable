package de.twomartens.timetable

import de.twomartens.timetable.bahnApi.service.ScheduledTaskService
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.scheduling.annotation.EnableScheduling

@EnableMongoAuditing
@EnableScheduling
@SpringBootApplication
open class MainApplication(
        private val scheduledTaskService: ScheduledTaskService
) {
    @EventListener(ApplicationReadyEvent::class)
    fun ready() {
        scheduledTaskService.initializeScheduledTasks()
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}

fun main(args: Array<String>) {
    runApplication<MainApplication>(*args)
}

