package de.twomartens.timetable.bahnApi.repository

import de.twomartens.timetable.bahnApi.model.db.ScheduledFetchTask
import org.springframework.data.mongodb.repository.MongoRepository

// TODO: what should be ID type be?
interface ScheduledFetchTaskRepository : MongoRepository<ScheduledFetchTask, String>
