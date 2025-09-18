package de.twomartens.timetable.bahnApi.repository

import de.twomartens.timetable.bahnApi.model.Eva
import de.twomartens.timetable.bahnApi.model.db.BahnTimetable
import org.bson.types.ObjectId
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.ZonedDateTime

interface BahnTimetableRepository : MongoRepository<BahnTimetable, ObjectId> {
    fun findByEvaAndHourAtDay(eva: Eva, hourAtDay: ZonedDateTime): BahnTimetable?

    fun getExampleMatcher(): ExampleMatcher {
        return ExampleMatcher.matching()
                .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("routeId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("tswTimetableId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("eva", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("hourAtDay", ExampleMatcher.GenericPropertyMatchers.exact())
    }

    fun getBahnTimetablesByUserIdAndTswTimetableId(userId: String, tswTimetableId: String,
                                                   pageable: Pageable): Page<BahnTimetable>
}