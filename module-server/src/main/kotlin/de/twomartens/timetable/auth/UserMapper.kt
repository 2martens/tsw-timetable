package de.twomartens.timetable.auth

import de.twomartens.timetable.model.dto.User
import org.mapstruct.*

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface UserMapper {

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "name", target = "name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    fun mapToDB(dto: User): de.twomartens.timetable.model.db.User

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "name", target = "name")
    fun mapToDto(db: de.twomartens.timetable.model.db.User): User
}