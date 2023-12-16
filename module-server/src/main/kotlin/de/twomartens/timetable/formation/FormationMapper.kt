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

    fun mapToDto(db: de.twomartens.timetable.model.db.Formation,
                 trainSimWorldFormation: de.twomartens.timetable.model.db.Formation?): Formation {

        val dtoTrainSimWorldFormation = if (trainSimWorldFormation != null) {
            mapToDto(trainSimWorldFormation, null)
        } else {
            null
        }

        return Formation(
                db.formationId,
                db.name,
                dtoTrainSimWorldFormation,
                db.formation,
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
                dto.trainSimWorldFormation?.id,
                dto.formation,
                dto.length
        )
    }
}