package de.twomartens.timetable.timetable

import de.twomartens.timetable.bahnApi.service.ScheduledTaskService
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.route.TswRouteRepository
import de.twomartens.timetable.types.NonEmptyString
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
@RequestMapping(value = ["/v1/timetables"])
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
    @PostMapping("/{userId}/{routeName}/fetchTimetable/{date}")
    fun scheduleFetch(
            @PathVariable @Parameter(description = "The id of the user",
                    example = "1",
                    required = true) userId: UserId,
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
        val route = routeRepository.findByUserIdAndName(userId, routeName)
                ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Route name must belong to existing route")
        scheduledTaskService.triggerTimetableFetch(route, date)
    }
}