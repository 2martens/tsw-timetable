package de.twomartens.timetable.support.controller

import de.twomartens.timetable.property.ServiceProperties
import io.swagger.v3.oas.annotations.Hidden
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/timetable"])
class HealthCheckController(private val properties: ServiceProperties) {


    @Hidden
    @GetMapping("/healthCheck")
    fun healthCheck(message: String): String {
        return properties.greeting.format(message)
    }
}