package de.twomartens.timetable.route

import de.twomartens.timetable.auth.UserRepository
import de.twomartens.timetable.model.common.RouteId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.model.dto.TswRoute
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

@RestController
@RequestMapping(value = ["/v1/routes"])
@Tag(name = "Routes", description = "all requests relating to routes")
class RouteController(
        private val routeRepository: TswRouteRepository,
        private val userRepository: UserRepository
) {
    private val mapper = Mappers.getMapper(RouteMapper::class.java)

    @Operation(
            summary = "Access routes of user and filter by name",
            responses = [ApiResponse(
                    responseCode = "200",
                    description = "List of found routes",
                    content = [Content(
                            array = ArraySchema(schema = Schema(implementation = TswRoute::class))
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
    fun getRoutes(
            @PathVariable @Parameter(description = "The id of the user",
                    example = "1",
                    required = true) userId: String,
            @RequestParam(name = "name", required = false) @Parameter(description = "Searched name",
                    example = "1",
                    required = false) name: String?
    ): ResponseEntity<List<TswRoute>> {
        val userIdConverted = UserId.of(NonEmptyString(userId))
        val userExists = userRepository.existsByUserId(userIdConverted)
        if (!userExists) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        val routes = if (!name.isNullOrBlank()) {
            routeRepository.findAllByUserIdAndNameContainingIgnoreCase(userIdConverted, name)
        } else {
            routeRepository.findAllByUserId(userIdConverted)
        }

        return ResponseEntity.ok(mapper.mapRoutesToDto(routes))
    }

    @Operation(
            summary = "Access route with id belonging to user",
            responses = [ApiResponse(
                    responseCode = "200",
                    description = "Route exists and was returned",
                    content = [Content(
                            schema = Schema(implementation = TswRoute::class)
                    )]
            ), ApiResponse(
                    responseCode = "404",
                    description = "Route was not found",
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
    fun getRoute(
            @PathVariable @Parameter(description = "The id of the user",
                    example = "1",
                    required = true) userId: UserId,
            @PathVariable @Parameter(description = "The id of the route",
                    example = "1",
                    required = true) id: RouteId
    ): ResponseEntity<TswRoute> {
        val userExists = userRepository.existsByUserId(userId)
        if (!userExists) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        val route = routeRepository.findByUserIdAndRouteId(userId, id)
                ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(mapper.mapToDto(route))
    }

    @Operation(
            summary = "Store route with id belonging to user",
            responses = [ApiResponse(
                    responseCode = "200",
                    description = "Route was updated",
                    content = [Content(
                            schema = Schema(implementation = TswRoute::class)
                    )]
            ), ApiResponse(
                    responseCode = "201",
                    description = "Route was created",
                    content = [Content(
                            schema = Schema(implementation = TswRoute::class)
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
    fun putRoute(
            @PathVariable @Parameter(description = "The id of the user",
                    example = "1",
                    required = true) userId: UserId,
            @PathVariable @Parameter(description = "The id of the route",
                    example = "1",
                    required = true) id: RouteId,
            @RequestBody(required = true) @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = [
                        Content(
                                schema = Schema(implementation = TswRoute::class)
                        )
                    ]) body: TswRoute
    ): ResponseEntity<TswRoute> {
        val userExists = userRepository.existsByUserId(userId)
        if (!userExists) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        var route = routeRepository.findByUserIdAndRouteId(userId, id)
        var created = false

        if (route == null) {
            created = true
            route = mapper.mapToDB(userId, body)
        } else {
            route.name = body.name
            route.country = body.country
            route.stations = body.stations
            route.depots = mapper.mapDepotsToDB(body.depots)
            route.portals = mapper.mapPortalsToDB(body.portals)
        }

        routeRepository.save(route)

        val updatedRoute = mapper.mapToDto(route)
        return if (created) {
            ResponseEntity.status(HttpStatus.CREATED).body(updatedRoute)
        } else {
            ResponseEntity.ok(updatedRoute)
        }
    }

    @Operation(
            summary = "Delete route with id belonging to user",
            responses = [ApiResponse(
                    responseCode = "204",
                    description = "Route was deleted",
                    content = [Content(mediaType = "text/plain")]
            ), ApiResponse(
                    responseCode = "404",
                    description = "Route was not found",
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
    fun deleteRoute(
            @PathVariable @Parameter(description = "The id of the user",
                    example = "1",
                    required = true) userId: UserId,
            @PathVariable @Parameter(description = "The id of the route",
                    example = "1",
                    required = true) id: RouteId,
    ): ResponseEntity<Void> {
        val userExists = userRepository.existsByUserId(userId)
        if (!userExists) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        val route = routeRepository.findByUserIdAndRouteId(userId, id)
                ?: return ResponseEntity.notFound().build()
        routeRepository.delete(route)

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}