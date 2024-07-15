package org.sidequest.parley.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.sidequest.parley.entity.EnrollmentEntity;
import org.sidequest.parley.model.Enrollment;

@Mapper(uses = {UserMapper.class, ChatRoomMapper.class})
public interface EnrollmentMapper {

    EnrollmentMapper INSTANCE = Mappers.getMapper(EnrollmentMapper.class);

    @Mapping(source = "chat_user.id", target = "userId")
    @Mapping(source = "chatroom.id", target = "chatRoomId")
    Enrollment toModel(EnrollmentEntity enrollmentEntity);

    @Mapping(source = "userId", target = "chat_user.id")
    @Mapping(source = "chatRoomId", target = "chatroom.id")
    EnrollmentEntity toEntity(Enrollment enrollment);
}
