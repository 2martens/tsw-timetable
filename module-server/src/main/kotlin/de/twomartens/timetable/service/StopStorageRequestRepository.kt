package de.twomartens.timetable.service

import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface StopStorageRequestRepository : MongoRepository<StopStorageRequest, ObjectId> {
    // is used
    fun getStopStorageRequestsByUserIdAndTimetableId(userId: String, timetableId: String, pageable: Pageable): Page<StopStorageRequest>
}