package de.twomartens.timetable.model.db

import de.twomartens.timetable.model.common.CountryCode
import de.twomartens.timetable.model.common.Platform
import de.twomartens.timetable.model.common.StationId
import de.twomartens.timetable.types.NonEmptyString
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
@CompoundIndex(def = "{stationId: 1, countryCode: 1}", unique = true)
data class Station(
        var stationId: StationId,
        var countryCode: CountryCode,
        var name: NonEmptyString,
) {
    @Id
    lateinit var id: ObjectId

    @CreatedDate
    lateinit var created: Instant

    @LastModifiedDate
    lateinit var lastModified: Instant

    private var platformsInternal: MutableMap<String, Platform> = mutableMapOf()

    val platforms: List<Platform> get() = platformsInternal.values.toList()

    fun addPlatform(platform: Platform?) {
        if (platform == null) {
            return
        }

        if (!platformsInternal.containsKey(platform.name)) {
            platformsInternal[platform.name] = platform
        } else {
            platformsInternal[platform.name]?.addAllSections(platform.sections)
        }
    }

    fun addAllPlatforms(platforms: Collection<Platform>) {
        platforms.forEach { addPlatform(it) }
    }
}
