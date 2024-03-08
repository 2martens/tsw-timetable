package de.twomartens.timetable.model.db

import de.twomartens.timetable.model.common.FormationId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.types.NonEmptyString
import de.twomartens.timetable.types.ZeroOrPositiveInteger
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
@CompoundIndex(def = "{'userId': 1, 'formationId': 1}", unique = true)
data class Formation(
        var userId: UserId,
        var formationId: FormationId,
        var name: NonEmptyString,
        var trainSimWorldFormationId: FormationId?,
        var formation: String,
        var length: ZeroOrPositiveInteger
) {
    @Id
    lateinit var id: ObjectId

    @CreatedDate
    lateinit var created: Instant

    @LastModifiedDate
    lateinit var lastModified: Instant
}
