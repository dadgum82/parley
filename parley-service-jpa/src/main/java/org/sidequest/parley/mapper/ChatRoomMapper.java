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

//package org.sidequest.parley.mapper;
//
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//import org.sidequest.parley.model.ChatRoom;
//import org.sidequest.parley.entity.ChatRoomEntity;
//
//@Mapper(uses = UserMapper.class) // Use UserMapper to map User and UserEntity
//public interface ChatRoomMapper {
//
//    ChatRoomMapper INSTANCE = Mappers.getMapper(ChatRoomMapper.class);
//
//    @Mapping(target = "id", source = "chatRoomId")
//    @Mapping(target = "chatRoomUsers", source = "users")
//    @Mapping(target = "moderator", source = "moderator")
//    @Mapping(target = "name", source = "name")
//    ChatRoomEntity mapTo(ChatRoom chatRoom);
//
//    @Mapping(target = "chatRoomId", source = "id")
//    @Mapping(target = "users", source = "chatRoomUsers")
//    @Mapping(target = "name", source = "name")
//    ChatRoom mapTo(ChatRoomEntity chatRoomEntity);
//
//    default ChatRoomEntity map(ChatRoom chatRoom) {
//        ChatRoomEntity chatRoomEntity = new ChatRoomEntity();
//        chatRoomEntity.setId(chatRoom.getChatRoomId());
//        chatRoomEntity.setName(chatRoom.getName());
//        chatRoomEntity.setModerator(UserMapper.INSTANCE.toEntity(chatRoom.getModerator())); // Use UserMapper to map User to UserEntity
//        return chatRoomEntity;
//    }
//
//    default ChatRoom map(ChatRoomEntity chatRoomEntity) {
//        ChatRoom chatRoom = new ChatRoom();
//        chatRoom.setChatRoomId(chatRoomEntity.getId());
//        chatRoom.setName(chatRoomEntity.getName());
//        chatRoom.setModerator(UserMapper.INSTANCE.toModel(chatRoomEntity.getModerator())); // Use UserMapper to map UserEntity to User
//        return chatRoom;
//    }
//
//}