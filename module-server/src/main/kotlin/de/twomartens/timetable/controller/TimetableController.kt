package de.twomartens.timetable.controller

import de.twomartens.timetable.model.NonEmptyString
import de.twomartens.timetable.repository.TswRouteRepository
import de.twomartens.timetable.service.ScheduledTaskService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.Clock
import java.time.LocalDate

@RestController
@RequestMapping(value = ["/timetable/v1"])
@Tag(name = "Timetables", description = "all requests relating to timetables")
class TimetableController(
        private val clock: Clock,
        private val routeRepository: TswRouteRepository,
        private val scheduledTaskService: ScheduledTaskService
) {
    @Operation(
            summary = "Task the backend with fetching a timetable for specified route and date",
            responses = [ApiResponse(
                    responseCode = "200",
                    description = "Timetable will be fetched as requested"
            ), ApiResponse(
                    responseCode = "400",
                    description = "Request does not follow specification"
            )]
    )
    @PostMapping("/{routeName}/fetchTimetable/{date}")
    fun scheduleFetch(
            @PathVariable @Parameter(description = "The name of the TSW route.",
                    example = "CologneAachen",
                    required = true) routeName: NonEmptyString,
            @PathVariable @Parameter(
                    description = "The date to fetch timetable for. Must be in the future.",
                    example = "2023-09-12",
                    required = true) date: LocalDate
    ) {
        if (!date.isAfter(LocalDate.now(clock))) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Date must be in the future")
        }
        val route = routeRepository.findByName(routeName)
                ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Route name must belong to existing route")
        scheduledTaskService.scheduleTimetableFetch(route, date)
    }
}