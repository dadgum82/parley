package org.sidequest.parley.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.repository.ChatRoomEntity;
import org.sidequest.parley.repository.ChatRoomUsersEntity;
import org.sidequest.parley.repository.UserEntity;

@Mapper
public interface ChatRoomMapper {

    public static ChatRoomMapper INSTANCE = Mappers.getMapper(ChatRoomMapper.class);

    //    @Mappings({
//            @Mapping(target = "id", source = "id"),
//            @Mapping(target = "userIds", source = "chatRoomUsers"),
//            @Mapping(target = "moderatorId", source = "moderatorId"),
//            @Mapping(target = "name", source = "name")
//    })
    @Mapping(target = "id", source = "chatRoomId")
    //@Mapping(target = "userIds", source = "userIds")
    ChatRoomEntity mapTo(ChatRoom chatRoom);

    //    @InheritInverseConfiguration
    @Mapping(target = "chatRoomId", source = "id")
//    @Mapping(target = "userIds", source = "userIds")
    ChatRoom mapTo(ChatRoomEntity chatRoomEntity);

//    @InheritInverseConfiguration
//    @Mapping(target = "chatRoomId", source = "id")
//    ChatRoom mapTo(Optional<ChatRoomEntity> chatRoomEntity);


    // Add these methods
    default Integer map(ChatRoomUsersEntity chatRoomUsersEntity) {
        return chatRoomUsersEntity.getUser().getId();
    }

    default ChatRoomUsersEntity map(Integer userId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        ChatRoomUsersEntity chatRoomUsersEntity = new ChatRoomUsersEntity();
        chatRoomUsersEntity.setUser(userEntity);
        return chatRoomUsersEntity;
    }
}