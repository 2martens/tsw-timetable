package de.twomartens.timetable.bahnApi.property

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "de.twomartens.timetable.bahn-api")
@Schema(description = "Properties to configure the BahnAPI")
data class BahnApiProperties @ConstructorBinding constructor(
        val clientId: String,
        val clientSecret: String
)
