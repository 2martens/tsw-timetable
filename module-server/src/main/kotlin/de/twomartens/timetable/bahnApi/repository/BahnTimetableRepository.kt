package de.twomartens.timetable.bahnApi.repository

import de.twomartens.timetable.bahnApi.model.db.BahnStation
import de.twomartens.timetable.bahnApi.model.db.BahnTimetable
import org.springframework.data.mongodb.repository.MongoRepository

interface BahnTimetableRepository : MongoRepository<BahnTimetable, String>
