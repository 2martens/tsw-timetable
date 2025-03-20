package de.twomartens.timetable.property

import de.twomartens.timetable.model.common.CountryCode
import de.twomartens.timetable.model.common.LanguageCode
import de.twomartens.timetable.types.NonEmptyString
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "de.twomartens.timetable")
@Schema(description = "Properties, to configure this Application")
open class ServiceProperties {
    lateinit var countryNames: Map<CountryCode, Map<LanguageCode, NonEmptyString>>
}
