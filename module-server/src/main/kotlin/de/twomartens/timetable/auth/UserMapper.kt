package de.twomartens.timetable.auth

import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.model.dto.User
import de.twomartens.timetable.types.Email
import de.twomartens.timetable.types.NonEmptyString
import org.mapstruct.*

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    fun mapToDB(dto: User): de.twomartens.timetable.model.db.User {
        return de.twomartens.timetable.model.db.User(
                UserId.of(NonEmptyString(dto.id)),
                NonEmptyString(dto.name),
                Email.of(NonEmptyString(dto.email))
        )
    }

    fun mapToDto(db: de.twomartens.timetable.model.db.User): User {
        return User(
                db.userId.value,
                db.name.value,
                db.email.value
        )
    }
}