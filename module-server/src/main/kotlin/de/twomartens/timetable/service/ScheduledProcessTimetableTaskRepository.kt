package de.twomartens.timetable.service

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant

interface ScheduledProcessTimetableTaskRepository : MongoRepository<ScheduledProcessTimetableTask, ObjectId> {
    fun findScheduledProcessTimetableTasksByExecutionTimeAfter(executionTime: Instant): List<ScheduledProcessTimetableTask>
}