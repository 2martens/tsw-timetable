package de.twomartens.timetable.bahnApi.repository

import de.twomartens.timetable.bahnApi.model.Eva
import de.twomartens.timetable.bahnApi.model.db.BahnTimetable
import de.twomartens.timetable.types.HourAtDay
import org.bson.types.ObjectId
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.mongodb.repository.MongoRepository

// TODO: figure out what ID type should be
interface BahnTimetableRepository : MongoRepository<BahnTimetable, ObjectId> {
    fun findByEvaAndHourAtDay(eva: Eva, hourAtDay: HourAtDay): BahnTimetable?

    fun getExampleMatcher(): ExampleMatcher {
        return ExampleMatcher.matching()
                .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("routeId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("eva", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("hourAtDay", ExampleMatcher.GenericPropertyMatchers.exact())
    }
}