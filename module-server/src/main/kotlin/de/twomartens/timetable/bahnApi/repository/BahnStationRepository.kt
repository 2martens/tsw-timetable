package de.twomartens.timetable.bahnApi.repository

import de.twomartens.timetable.bahnApi.model.db.BahnStation
import org.springframework.data.mongodb.repository.MongoRepository

interface BahnStationRepository : MongoRepository<BahnStation, String>
