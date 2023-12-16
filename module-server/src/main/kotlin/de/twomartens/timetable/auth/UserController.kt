package de.twomartens.timetable.auth

import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.model.dto.User
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
@RequestMapping(value = ["/v1/users"])
@Tag(name = "Users", description = "all requests relating to user resources")
class UserController(
        private val userRepository: UserRepository
) {

    private val mapper = Mappers.getMapper(UserMapper::class.java)

    @Operation(
            summary = "Store user",
            responses = [ApiResponse(
                    responseCode = "200",
                    description = "User was updated",
                    content = [Content(
                            schema = Schema(implementation = User::class)
                    )]
            ), ApiResponse(
                    responseCode = "201",
                    description = "User was created",
                    content = [Content(
                            schema = Schema(implementation = User::class)
                    )]
            ), ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden for user",
                    content = [Content(mediaType = "text/plain")]
            )]
    )
    @PutMapping("/{userId}")
    fun putFormation(
            @PathVariable @Parameter(description = "The id of the user",
                    example = "1",
                    required = true) userId: UserId,
            @RequestBody(required = true) @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = [
                        Content(
                                schema = Schema(implementation = User::class)
                        )
                    ]) body: User
    ): ResponseEntity<User> {
        var created = false

        var user = userRepository.findByUserId(userId)
        if (user == null) {
            created = true
            user = mapper.mapToDB(body)
        } else {
            user.name = body.name
        }

        userRepository.save(user)

        val updatedUser = mapper.mapToDto(user)
        return if (created) {
            ResponseEntity.status(HttpStatus.CREATED).body(updatedUser)
        } else {
            ResponseEntity.ok(updatedUser)
        }
    }
}