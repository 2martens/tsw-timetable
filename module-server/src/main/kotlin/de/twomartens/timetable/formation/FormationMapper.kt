package de.twomartens.timetable.formation

import de.twomartens.timetable.model.common.FormationId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.model.dto.Formation
import de.twomartens.timetable.types.NonEmptyString
import de.twomartens.timetable.types.ZeroOrPositiveInteger
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
                db.formationId.id,
                db.name.value,
                dtoTrainSimWorldFormation,
                db.formation,
                db.length.value
        )
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    fun mapToDB(userId: UserId, dto: Formation): de.twomartens.timetable.model.db.Formation {
        return de.twomartens.timetable.model.db.Formation(
                userId,
                FormationId.of(NonEmptyString(dto.id)),
                NonEmptyString(dto.name),
                if (dto.trainSimWorldFormation != null)
                    FormationId.of(NonEmptyString(dto.trainSimWorldFormation!!.id)) else null,
                dto.formation,
                ZeroOrPositiveInteger(dto.length)
        )
    }
}