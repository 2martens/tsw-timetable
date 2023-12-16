package de.twomartens.timetable.formation

import de.twomartens.timetable.auth.UserRepository
import de.twomartens.timetable.model.common.FormationId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.model.dto.Formation
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.mapstruct.factory.Mappers
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/v1/formations"])
@Tag(name = "Formations", description = "all requests relating to formation resources")
class FormationController(
        private val formationRepository: FormationRepository,
        private val userRepository: UserRepository
) {
    private val mapper = Mappers.getMapper(FormationMapper::class.java)

    @Operation(
            summary = "Access formation with id belonging to user",
            responses = [ApiResponse(
                    responseCode = "200",
                    description = "Formation exists and was returned",
                    content = [Content(
                            schema = Schema(implementation = Formation::class)
                    )]
            ), ApiResponse(
                    responseCode = "404",
                    description = "UserId or formation were not found",
                    content = [Content(mediaType = "text/plain")]
            )]
    )
    @GetMapping("/{userId}/{id}")
    fun getFormation(
            @PathVariable @Parameter(description = "The id of the user",
                    example = "1",
                    required = true) userId: UserId,
            @PathVariable @Parameter(description = "The id of the formation",
                    example = "1",
                    required = true) id: FormationId
    ): ResponseEntity<Formation> {
        val formation = formationRepository.findByUserIdAndFormationId(userId, id)
                ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(mapper.mapToDto(formation))
    }

    @Operation(
            summary = "Store formation with id belonging to user",
            responses = [ApiResponse(
                    responseCode = "200",
                    description = "Formation was updated",
                    content = [Content(
                            schema = Schema(implementation = Formation::class)
                    )]
            ), ApiResponse(
                    responseCode = "201",
                    description = "Formation was created",
                    content = [Content(
                            schema = Schema(implementation = Formation::class)
                    )]
            ), ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden for user",
                    content = [Content(mediaType = "text/plain")]
            )]
    )
    @PutMapping("/{userId}/{id}")
    fun putFormation(
            @PathVariable @Parameter(description = "The id of the user",
                    example = "1",
                    required = true) userId: UserId,
            @PathVariable @Parameter(description = "The id of the formation",
                    example = "1",
                    required = true) id: FormationId,
            @RequestBody(required = true) @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = [
                        Content(
                                schema = Schema(implementation = Formation::class)
                        )
                    ]) body: Formation
    ): ResponseEntity<Formation> {
        val userExists = userRepository.existsByUserId(userId)
        if (!userExists) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        var formation = formationRepository.findByUserIdAndFormationId(userId, id)
        var created = false

        if (formation == null) {
            created = true
            formation = mapper.mapToDB(userId, body)
        } else {
            formation.name = body.name
            formation.trainSimWorldFormationId = body.trainSimWorldFormationId
            formation.coaches = body.coaches
            formation.length = body.length
        }

        formationRepository.save(formation)

        val updatedFormation = mapper.mapToDto(formation)
        return if (created) {
            ResponseEntity.status(HttpStatus.CREATED).body(updatedFormation)
        } else {
            ResponseEntity.ok(updatedFormation)
        }
    }
}