package de.twomartens.timetable.formation

import de.twomartens.timetable.auth.UserRepository
import de.twomartens.timetable.model.common.FormationId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.model.dto.Formation
import de.twomartens.timetable.types.NonEmptyString
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
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
            summary = "Access formations of user and filter by name",
            responses = [ApiResponse(
                    responseCode = "200",
                    description = "List of found formations",
                    content = [Content(
                            array = ArraySchema(schema = Schema(implementation = Formation::class))
                    )]
            ), ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden for user",
                    content = [Content(mediaType = "text/plain")]
            )]
    )
    @GetMapping("/{userId}/")
    fun getFormations(
            @PathVariable @Parameter(description = "The id of the user",
                    example = "1",
                    required = true) userId: String,
            @RequestParam(name = "name", required = false) @Parameter(description = "Searched name",
                    example = "ICE",
                    required = false) name: String?
    ): ResponseEntity<List<Formation>> {
        val userIdConverted = UserId.of(NonEmptyString(userId))
        val userExists = userRepository.existsByUserId(userIdConverted)
        if (!userExists) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        val formations = if (!name.isNullOrBlank()) {
            formationRepository.findAllByUserIdAndNameContainingIgnoreCase(userIdConverted, name)
        } else {
            formationRepository.findAllByUserId(userIdConverted)
        }

        val formationsDto = formations.map {
            val trainSimWorldFormation = if (it.trainSimWorldFormationId != null) {
                formationRepository.findByUserIdAndFormationId(userIdConverted, it.trainSimWorldFormationId!!)
            } else {
                null
            }
            mapper.mapToDto(it, trainSimWorldFormation)
        }
        return ResponseEntity.ok(formationsDto)
    }

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
                    description = "Formation was not found",
                    content = [Content(mediaType = "text/plain")]
            ), ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden for user",
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
        val userExists = userRepository.existsByUserId(userId)
        if (!userExists) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        val formation = formationRepository.findByUserIdAndFormationId(userId, id)
                ?: return ResponseEntity.notFound().build()
        val trainSimWorldFormation = if (formation.trainSimWorldFormationId != null) {
            formationRepository.findByUserIdAndFormationId(userId, formation.trainSimWorldFormationId!!)
        } else {
            null
        }
        return ResponseEntity.ok(mapper.mapToDto(formation, trainSimWorldFormation))
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
        val trainSimWorldFormation = createOrUpdateTrainSimWorldFormation(userId, body.trainSimWorldFormation)
        if (trainSimWorldFormation != null) {
            formationRepository.save(trainSimWorldFormation)
        }

        var created = false

        if (formation == null) {
            created = true
            formation = mapper.mapToDB(userId, body)
        } else {
            formation.name = body.name
            formation.trainSimWorldFormationId = body.trainSimWorldFormation?.id
            formation.formation = body.formation
            formation.length = body.length
        }

        formationRepository.save(formation)

        val updatedFormation = mapper.mapToDto(formation, trainSimWorldFormation)
        return if (created) {
            ResponseEntity.status(HttpStatus.CREATED).body(updatedFormation)
        } else {
            ResponseEntity.ok(updatedFormation)
        }
    }

    private fun createOrUpdateTrainSimWorldFormation(
            userId: UserId,
            formation: Formation?
    ): de.twomartens.timetable.model.db.Formation? {
        if (formation == null) {
            return null
        }

        var trainSimWorldFormation = formationRepository.findByUserIdAndFormationId(
                userId, formation.id)
        if (trainSimWorldFormation == null) {
            trainSimWorldFormation = mapper.mapToDB(userId, formation)
            trainSimWorldFormation.trainSimWorldFormationId = null
        } else {
            trainSimWorldFormation.name = formation.name
            trainSimWorldFormation.trainSimWorldFormationId = null
            trainSimWorldFormation.formation = formation.formation
            trainSimWorldFormation.length = formation.length
        }
        return trainSimWorldFormation
    }

    @Operation(
            summary = "Delete formation with id belonging to user",
            responses = [ApiResponse(
                    responseCode = "204",
                    description = "Formation was deleted",
                    content = [Content(mediaType = "text/plain")]
            ), ApiResponse(
                    responseCode = "404",
                    description = "Formation was not found",
                    content = [Content(mediaType = "text/plain")]
            ), ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden for user",
                    content = [Content(mediaType = "text/plain")]
            )]
    )
    @DeleteMapping("/{userId}/{id}")
    fun deleteFormation(
            @PathVariable @Parameter(description = "The id of the user",
                    example = "1",
                    required = true) userId: UserId,
            @PathVariable @Parameter(description = "The id of the formation",
                    example = "1",
                    required = true) id: FormationId,
    ): ResponseEntity<Void> {
        val userExists = userRepository.existsByUserId(userId)
        if (!userExists) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        val formation = formationRepository.findByUserIdAndFormationId(userId, id)
                ?: return ResponseEntity.notFound().build()
        formationRepository.delete(formation)

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}