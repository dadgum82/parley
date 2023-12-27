package org.sidequest.parley.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.sidequest.parley.entity.ChatRoomsUsersEntity;
import org.sidequest.parley.model.ChatRoomUsers;

@Mapper
public interface ChatRoomsUsersMapper {

    ChatRoomsUsersMapper INSTANCE = Mappers.getMapper(ChatRoomsUsersMapper.class);

    //    @Mapping(target = "id", source = "id")
    @Mapping(target = "user", source = "userId")
    @Mapping(target = "chatRoom", source = "chatRoomId")
    ChatRoomsUsersEntity mapTo(ChatRoomUsers chatRoomUsers);

    //    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "user")
    @Mapping(target = "chatRoomId", source = "chatRoom")
    ChatRoomUsers mapTo(ChatRoomsUsersEntity chatRoomUsersEntity);
}