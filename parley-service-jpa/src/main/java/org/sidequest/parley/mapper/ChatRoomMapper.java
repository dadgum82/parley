package org.sidequest.parley.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.sidequest.parley.entity.ChatRoomEntity;
import org.sidequest.parley.model.ChatRoom;

@Mapper(uses = UserMapper.class)
public interface ChatRoomMapper {

    ChatRoomMapper INSTANCE = Mappers.getMapper(ChatRoomMapper.class);

    @Mapping(target = "id", source = "chatRoomId")
    @Mapping(target = "chatRoomUsers", source = "users")
    @Mapping(target = "moderator", source = "moderator")
    @Mapping(target = "name", source = "name")
    ChatRoomEntity toEntity(ChatRoom chatRoom);

    @Mapping(target = "chatRoomId", source = "id")
    @Mapping(target = "users", source = "chatRoomUsers")
    @Mapping(target = "name", source = "name")
    ChatRoom toModel(ChatRoomEntity chatRoomEntity);
}