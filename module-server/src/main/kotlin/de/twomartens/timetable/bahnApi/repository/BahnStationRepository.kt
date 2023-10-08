package de.twomartens.timetable.bahnApi.repository

import de.twomartens.timetable.bahnApi.model.db.BahnStation
import org.springframework.data.mongodb.repository.MongoRepository

// TODO: figure out what ID type should be
interface BahnStationRepository : MongoRepository<BahnStation, String>
