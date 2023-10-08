package de.twomartens.timetable.bahnApi.repository

import de.twomartens.timetable.bahnApi.model.db.BahnTimetable
import org.springframework.data.mongodb.repository.MongoRepository

// TODO: figure out what ID type should be
interface BahnTimetableRepository : MongoRepository<BahnTimetable, String>