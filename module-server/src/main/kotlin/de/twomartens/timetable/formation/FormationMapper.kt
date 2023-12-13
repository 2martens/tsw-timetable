package de.twomartens.timetable.formation

import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.model.dto.Formation
import org.mapstruct.*

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface FormationMapper {

    fun mapToDto(db: de.twomartens.timetable.model.db.Formation): Formation {
        return Formation(
                db.formationId,
                db.name,
                db.trainSimWorldFormationId,
                db.coaches,
                db.length
        )
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    fun mapToDB(userId: UserId, dto: Formation): de.twomartens.timetable.model.db.Formation {
        return de.twomartens.timetable.model.db.Formation(
                userId,
                dto.id,
                dto.name,
                dto.trainSimWorldFormationId,
                dto.coaches,
                dto.length
        )
    }
}