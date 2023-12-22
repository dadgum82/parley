package org.sidequest.parley.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.repository.ChatRoomEntity;

@Mapper
public interface ChatRoomMapper {

    ChatRoomMapper INSTANCE = Mappers.getMapper(ChatRoomMapper.class);

    @Mapping(target = "id", source = "chatRoomId")
    @Mapping(target = "chatRoomUsers", source = "users")
    @Mapping(target = "moderatorId", source = "moderatorId")
    @Mapping(target = "name", source = "name")
    ChatRoomEntity mapTo(ChatRoom chatRoom);

    @Mapping(target = "chatRoomId", source = "id")
    @Mapping(target = "users", source = "chatRoomUsers")
    @Mapping(target = "name", source = "name")
    ChatRoom mapTo(ChatRoomEntity chatRoomEntity);

    default ChatRoomEntity map(ChatRoom chatRoom) {
        ChatRoomEntity chatRoomEntity = new ChatRoomEntity();
        chatRoomEntity.setId(chatRoom.getChatRoomId());
        chatRoomEntity.setName(chatRoom.getName());
        chatRoomEntity.setModeratorId(chatRoom.getModeratorId());
        return chatRoomEntity;
    }

    default ChatRoom map(ChatRoomEntity chatRoomEntity) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatRoomId(chatRoomEntity.getId());
        chatRoom.setName(chatRoomEntity.getName());
        chatRoom.setModeratorId(chatRoomEntity.getModeratorId());
        return chatRoom;
    }

}