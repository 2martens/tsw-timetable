package de.twomartens.timetable.station

import de.twomartens.timetable.bahnApi.service.BahnApiService
import de.twomartens.timetable.bahnApi.service.BahnDatabaseService
import de.twomartens.timetable.model.dto.Station
import de.twomartens.timetable.model.mapper.StationMapper
import de.twomartens.timetable.model.repository.StationRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.mapstruct.factory.Mappers
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/v1/stations"])
@Tag(name = "Stations", description = "all requests relating to stations")
class StationController(
        private val stationRepository: StationRepository,
        private val bahnApiService: BahnApiService,
        private val bahnDatabaseService: BahnDatabaseService
) {
    private val mapper = Mappers.getMapper(StationMapper::class.java)

    @Operation(
            summary = "Access stations and filter by name",
            responses = [ApiResponse(
                    responseCode = "200",
                    description = "List of found stations",
                    content = [Content(
                            array = ArraySchema(schema = Schema(implementation = Station::class))
                    )]
            ), ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden for user",
                    content = [Content(mediaType = "text/plain")]
            )]
    )
    @GetMapping("/")
    fun getStations(
            @RequestParam(name = "country", required = false) @Parameter(description = "Searched country",
                    example = "de",
                    required = false) countryCode: String?,
            @RequestParam(name = "name", required = false) @Parameter(description = "Searched name",
                    example = "1",
                    required = false) name: String?
    ): ResponseEntity<List<Station>> {
        val stations = findStations(countryCode, name)

        return ResponseEntity.ok(mapper.mapStationsToDto(stations))
    }

    private fun findStations(
            countryCode: String?,
            name: String?
    ): List<de.twomartens.timetable.model.db.Station> {
        val stations = if (!name.isNullOrBlank()) {
            if (!countryCode.isNullOrBlank()) {
                stationRepository.findAllByCountryCodeAndNameContainingIgnoreCase(countryCode, name)
            } else {
                stationRepository.findAllByNameContainingIgnoreCase(name)
            }
        } else {
            stationRepository.findAll()
        }
        return stations
    }

    @Operation(
            summary = "Update stations",
            responses = [ApiResponse(
                    responseCode = "200",
                    description = "Stations updated",
                    content = [Content(mediaType = "text/plain")]
            ), ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden for user",
                    content = [Content(mediaType = "text/plain")]
            )]
    )
    @PostMapping("/update")
    fun updateStations(): ResponseEntity<Void> {
        val stations = bahnApiService.fetchStations("")
        bahnDatabaseService.storeStations(stations)

        return ResponseEntity.ok().build()
    }
}