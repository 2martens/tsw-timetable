package de.twomartens.timetable.formation

import de.twomartens.timetable.model.common.FormationId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.model.db.Formation
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface FormationRepository : MongoRepository<Formation, ObjectId> {
    fun findByUserIdAndFormationId(userId: UserId, formationId: FormationId): Formation?
    fun findAllByUserId(userId: UserId): List<Formation>
    fun findAllByUserIdAndNameContainingIgnoreCase(userId: UserId, name: String): List<Formation>
}