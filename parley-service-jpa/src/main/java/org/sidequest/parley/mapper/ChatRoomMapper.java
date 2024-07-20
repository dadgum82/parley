package org.sidequest.parley.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.sidequest.parley.entity.ChatRoomEntity;
import org.sidequest.parley.model.ChatRoom;


//@Mapper(uses = {UserMapper.class, EnrollmentMapper.class})
@Mapper
public interface ChatRoomMapper {

    ChatRoomMapper INSTANCE = Mappers.getMapper(ChatRoomMapper.class);

    @Mapping(source = "id", target = "chatRoomId")
        // This is the only mapping that is different from the default
    ChatRoom toModel(ChatRoomEntity entity);

    @Mapping(source = "chatRoomId", target = "id")
        // This is the only mapping that is different from the default
    ChatRoomEntity toEntity(ChatRoom model);


}