package de.twomartens.timetable.model.db

import de.twomartens.timetable.model.common.ServiceId
import de.twomartens.timetable.model.common.TimetableId
import de.twomartens.timetable.model.common.UserId
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ServiceRepository : MongoRepository<Service, ObjectId> {
    fun existsServiceByUserIdAndTimetableIdAndServiceId(userId: UserId, timetableId: TimetableId, serviceId: ServiceId): Boolean
    fun getServiceByUserIdAndTimetableIdAndServiceId(userId: UserId, timetableId: TimetableId, serviceId: ServiceId): Service?
}
