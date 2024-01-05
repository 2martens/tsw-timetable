package de.twomartens.timetable.timetable

import de.twomartens.timetable.auth.UserRepository
import de.twomartens.timetable.bahnApi.service.ScheduledTaskService
import de.twomartens.timetable.model.common.TimetableId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.model.dto.Timetable
import de.twomartens.timetable.model.dto.TimetableState
import de.twomartens.timetable.route.TswRouteRepository
import de.twomartens.timetable.types.NonEmptyString
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.mapstruct.factory.Mappers
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.Clock
import java.time.LocalDate

@RestController
@RequestMapping(value = ["/v1/timetables"])
@Tag(name = "Timetables", description = "all requests relating to timetables")
class TimetableController(
        private val clock: Clock,
        private val routeRepository: TswRouteRepository,
        private val timetableRepository: TimetableRepository,
        private val userRepository: UserRepository,
        private val scheduledTaskService: ScheduledTaskService
) {

    private val mapper = Mappers.getMapper(TimetableMapper::class.java)

    @Operation(
            summary = "Access timetables of user and filter by name",
            responses = [ApiResponse(
                    responseCode = "200",
                    description = "List of found timetables",
                    content = [Content(
                            array = ArraySchema(schema = Schema(implementation = Timetable::class))
                    )]
            ), ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden for user",
                    content = [Content(mediaType = "text/plain")]
            )]
    )
    @SecurityRequirement(name = "bearer")
    @SecurityRequirement(name = "oauth2")
    @GetMapping("/{userId}/")
    fun getTimetables(
            @PathVariable @Parameter(description = "The id of the user",
                    example = "1",
                    required = true) userId: String,
            @RequestParam(name = "name", required = false) @Parameter(description = "Searched name",
                    example = "KAH",
                    required = false) name: String?
    ): ResponseEntity<List<Timetable>> {
        val userIdConverted = UserId.of(NonEmptyString(userId))
        val userExists = userRepository.existsByUserId(userIdConverted)
        if (!userExists) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        val timetables = if (!name.isNullOrBlank()) {
            timetableRepository.findAllByUserIdAndNameContainingIgnoreCase(userIdConverted, name)
        } else {
            timetableRepository.findAllByUserId(userIdConverted)
        }

        return ResponseEntity.ok(mapper.mapTimetablesToDto(timetables))
    }

    @Operation(
            summary = "Access timetable with id belonging to user",
            responses = [ApiResponse(
                    responseCode = "200",
                    description = "Timetable exists and was returned",
                    content = [Content(
                            schema = Schema(implementation = Timetable::class)
                    )]
            ), ApiResponse(
                    responseCode = "404",
                    description = "Timetable was not found",
                    content = [Content(mediaType = "text/plain")]
            ), ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden for user",
                    content = [Content(mediaType = "text/plain")]
            )]
    )
    @SecurityRequirement(name = "bearer")
    @SecurityRequirement(name = "oauth2")
    @GetMapping("/{userId}/{id}")
    fun getTimetable(
            @PathVariable @Parameter(description = "The id of the user",
                    example = "1",
                    required = true) userId: UserId,
            @PathVariable @Parameter(description = "The id of the timetable",
                    example = "1",
                    required = true) id: TimetableId
    ): ResponseEntity<Timetable> {
        val userExists = userRepository.existsByUserId(userId)
        if (!userExists) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        val timetable = timetableRepository.findByUserIdAndTimetableId(userId, id)
                ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(mapper.mapToDto(timetable))
    }

    @Operation(
            summary = "Store timetable with id belonging to user",
            responses = [ApiResponse(
                    responseCode = "200",
                    description = "Timetable was updated",
                    content = [Content(
                            schema = Schema(implementation = Timetable::class)
                    )]
            ), ApiResponse(
                    responseCode = "201",
                    description = "Timetable was created",
                    content = [Content(
                            schema = Schema(implementation = Timetable::class)
                    )]
            ), ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden for user",
                    content = [Content(mediaType = "text/plain")]
            )]
    )
    @SecurityRequirement(name = "bearer")
    @SecurityRequirement(name = "oauth2")
    @PutMapping("/{userId}/{id}")
    fun putTimetable(
            @PathVariable @Parameter(description = "The id of the user",
                    example = "1",
                    required = true) userId: String,
            @PathVariable @Parameter(description = "The id of the timetable",
                    example = "1",
                    required = true) id: String,
            @RequestBody(required = true) @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = [
                        Content(
                                schema = Schema(implementation = Timetable::class)
                        )
                    ]) body: Timetable
    ): ResponseEntity<Timetable> {
        val userIdConverted = UserId.of(NonEmptyString(userId))
        val timetableIdConverted = TimetableId.of(NonEmptyString(id))
        val userExists = userRepository.existsByUserId(userIdConverted)
        if (!userExists) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        var timetable = timetableRepository.findByUserIdAndTimetableId(userIdConverted, timetableIdConverted)
        var created = false

        if (timetable == null) {
            created = true
            timetable = mapper.mapToDB(userIdConverted, body)
            timetable.timetableState = TimetableState.PROCESSING
        } else {
            timetable.name = NonEmptyString(body.name)
        }

        timetableRepository.save(timetable)

        val updatedTimetable = mapper.mapToDto(timetable)
        return if (created) {
            ResponseEntity.status(HttpStatus.CREATED).body(updatedTimetable)
        } else {
            ResponseEntity.ok(updatedTimetable)
        }
    }

    @Operation(
            summary = "Delete timetable with id belonging to user",
            responses = [ApiResponse(
                    responseCode = "204",
                    description = "Timetable was deleted",
                    content = [Content(mediaType = "text/plain")]
            ), ApiResponse(
                    responseCode = "404",
                    description = "Timetable was not found",
                    content = [Content(mediaType = "text/plain")]
            ), ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden for user",
                    content = [Content(mediaType = "text/plain")]
            )]
    )
    @SecurityRequirement(name = "bearer")
    @SecurityRequirement(name = "oauth2")
    @DeleteMapping("/{userId}/{id}")
    fun deleteTimetable(
            @PathVariable @Parameter(description = "The id of the user",
                    example = "1",
                    required = true) userId: String,
            @PathVariable @Parameter(description = "The id of the timetable",
                    example = "1",
                    required = true) id: String,
    ): ResponseEntity<Void> {
        val userIdConverted = UserId.of(NonEmptyString(userId))
        val timetableIdConverted = TimetableId.of(NonEmptyString(id))
        val userExists = userRepository.existsByUserId(userIdConverted)
        if (!userExists) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        val timetable = timetableRepository.findByUserIdAndTimetableId(userIdConverted, timetableIdConverted)
                ?: return ResponseEntity.notFound().build()
        timetableRepository.delete(timetable)

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

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
    @SecurityRequirement(name = "bearer")
    @SecurityRequirement(name = "oauth2")
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