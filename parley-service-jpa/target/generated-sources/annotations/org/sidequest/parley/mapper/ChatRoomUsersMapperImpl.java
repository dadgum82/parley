package org.sidequest.parley.mapper;

import javax.annotation.processing.Generated;
import org.sidequest.parley.entity.ChatRoomEntity;
import org.sidequest.parley.entity.ChatRoomUsersEntity;
import org.sidequest.parley.entity.UserEntity;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.model.ChatRoomUsers;
import org.sidequest.parley.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-24T10:43:10-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
public class ChatRoomUsersMapperImpl implements ChatRoomUsersMapper {

    @Override
    public ChatRoomUsersEntity mapTo(ChatRoomUsers chatRoomUsers) {
        if ( chatRoomUsers == null ) {
            return null;
        }

        ChatRoomUsersEntity chatRoomUsersEntity = new ChatRoomUsersEntity();

        chatRoomUsersEntity.setUser( userToUserEntity( chatRoomUsers.getUserId() ) );
        chatRoomUsersEntity.setChatRoom( chatRoomToChatRoomEntity( chatRoomUsers.getChatRoomId() ) );
        chatRoomUsersEntity.setId( chatRoomUsers.getId() );

        return chatRoomUsersEntity;
    }

    @Override
    public ChatRoomUsers mapTo(ChatRoomUsersEntity chatRoomUsersEntity) {
        if ( chatRoomUsersEntity == null ) {
            return null;
        }

        ChatRoomUsers chatRoomUsers = new ChatRoomUsers();

        chatRoomUsers.setUserId( userEntityToUser( chatRoomUsersEntity.getUser() ) );
        chatRoomUsers.setChatRoomId( chatRoomEntityToChatRoom( chatRoomUsersEntity.getChatRoom() ) );
        chatRoomUsers.setId( chatRoomUsersEntity.getId() );

        return chatRoomUsers;
    }

    protected UserEntity userToUserEntity(User user) {
        if ( user == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( user.getId() );
        userEntity.setName( user.getName() );

        return userEntity;
    }

    protected ChatRoomEntity chatRoomToChatRoomEntity(ChatRoom chatRoom) {
        if ( chatRoom == null ) {
            return null;
        }

        ChatRoomEntity chatRoomEntity = new ChatRoomEntity();

        chatRoomEntity.setModerator( userToUserEntity( chatRoom.getModerator() ) );
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
        chatRoom.setModerator( userEntityToUser( chatRoomEntity.getModerator() ) );

        return chatRoom;
    }
}
