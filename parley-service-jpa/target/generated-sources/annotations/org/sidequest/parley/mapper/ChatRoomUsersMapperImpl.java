package org.sidequest.parley.mapper;

import javax.annotation.processing.Generated;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.model.ChatRoomUsers;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.ChatRoomEntity;
import org.sidequest.parley.repository.ChatRoomUsersEntity;
import org.sidequest.parley.repository.UserEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-22T14:01:47-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
public class ChatRoomUsersMapperImpl implements ChatRoomUsersMapper {

    @Override
    public ChatRoomUsersEntity mapTo(ChatRoomUsers chatRoomUsers) {
        if ( chatRoomUsers == null ) {
            return null;
        }

        ChatRoomUsersEntity chatRoomUsersEntity = new ChatRoomUsersEntity();

        if ( chatRoomUsers.getId() != null ) {
            chatRoomUsersEntity.setId( chatRoomUsers.getId() );
        }
        chatRoomUsersEntity.setUser( userToUserEntity( chatRoomUsers.getUserId() ) );
        chatRoomUsersEntity.setChatRoom( chatRoomToChatRoomEntity( chatRoomUsers.getChatRoomId() ) );

        return chatRoomUsersEntity;
    }

    @Override
    public ChatRoomUsers mapTo(ChatRoomUsersEntity chatRoomUsersEntity) {
        if ( chatRoomUsersEntity == null ) {
            return null;
        }

        ChatRoomUsers chatRoomUsers = new ChatRoomUsers();

        chatRoomUsers.setId( chatRoomUsersEntity.getId() );
        chatRoomUsers.setUserId( userEntityToUser( chatRoomUsersEntity.getUser() ) );
        chatRoomUsers.setChatRoomId( chatRoomEntityToChatRoom( chatRoomUsersEntity.getChatRoom() ) );

        return chatRoomUsers;
    }

    protected UserEntity userToUserEntity(User user) {
        if ( user == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        if ( user.getId() != null ) {
            userEntity.setId( user.getId() );
        }
        userEntity.setName( user.getName() );

        return userEntity;
    }

    protected ChatRoomEntity chatRoomToChatRoomEntity(ChatRoom chatRoom) {
        if ( chatRoom == null ) {
            return null;
        }

        ChatRoomEntity chatRoomEntity = new ChatRoomEntity();

        if ( chatRoom.getModeratorId() != null ) {
            chatRoomEntity.setModeratorId( chatRoom.getModeratorId() );
        }
        chatRoomEntity.setName( chatRoom.getName() );

        return chatRoomEntity;
    }

    protected User userEntityToUser(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        User user = new User();

        user.setId( userEntity.getId() );
        user.setName( userEntity.getName() );

        return user;
    }

    protected ChatRoom chatRoomEntityToChatRoom(ChatRoomEntity chatRoomEntity) {
        if ( chatRoomEntity == null ) {
            return null;
        }

        ChatRoom chatRoom = new ChatRoom();

        chatRoom.setName( chatRoomEntity.getName() );
        chatRoom.setModeratorId( chatRoomEntity.getModeratorId() );

        return chatRoom;
    }
}
