package org.sidequest.parley.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.sidequest.parley.model.ChatRoomUsers;
import org.sidequest.parley.repository.ChatRoomUsersEntity;

@Mapper
public interface ChatRoomUsersMapper {

    ChatRoomUsersMapper INSTANCE = Mappers.getMapper(ChatRoomUsersMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "user", source = "userId")
    @Mapping(target = "chatRoom", source = "chatRoomId")
    ChatRoomUsersEntity mapTo(ChatRoomUsers chatRoomUsers);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "user")
    @Mapping(target = "chatRoomId", source = "chatRoom")
    ChatRoomUsers mapTo(ChatRoomUsersEntity chatRoomUsersEntity);
}