package org.sidequest.parley.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.sidequest.parley.entity.EnrollmentEntity;
import org.sidequest.parley.model.Enrollment;

@Mapper
public interface EnrollmentMapper {

    EnrollmentMapper INSTANCE = Mappers.getMapper(EnrollmentMapper.class);

    //This is the only mapping that is different from the default
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "chatroom.id", target = "chatRoomId")
    Enrollment toModel(EnrollmentEntity entity);

    //This is the only mapping that is different from the default
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "chatRoomId", target = "chatroom.id")
    EnrollmentEntity toEntity(Enrollment model);
}
