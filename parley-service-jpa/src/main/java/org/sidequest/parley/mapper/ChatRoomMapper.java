package org.sidequest.parley.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.ChatRoomEntity;
import org.sidequest.parley.repository.UserEntity;

@Mapper
public interface ChatRoomMapper {

    ChatRoomMapper INSTANCE = Mappers.getMapper(ChatRoomMapper.class);

    @Mapping(target = "id", source = "chatRoomId")
    @Mapping(target = "users", source = "users")
    ChatRoomEntity mapTo(ChatRoom chatRoom);

    @Mapping(target = "chatRoomId", source = "id")
    @Mapping(target = "users", source = "users")
    ChatRoom mapTo(ChatRoomEntity chatRoomEntity);

    default UserEntity map(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        return userEntity;
    }

    default User map(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        return user;
    }
}