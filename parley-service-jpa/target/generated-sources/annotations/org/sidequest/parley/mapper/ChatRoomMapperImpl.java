package org.sidequest.parley.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.ChatRoomEntity;
import org.sidequest.parley.repository.UserEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-22T09:13:39-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
public class ChatRoomMapperImpl implements ChatRoomMapper {

    @Override
    public ChatRoomEntity mapTo(ChatRoom chatRoom) {
        if ( chatRoom == null ) {
            return null;
        }

        ChatRoomEntity chatRoomEntity = new ChatRoomEntity();

        if ( chatRoom.getChatRoomId() != null ) {
            chatRoomEntity.setId( chatRoom.getChatRoomId() );
        }
        chatRoomEntity.setUsers( userListToUserEntityList( chatRoom.getUsers() ) );
        if ( chatRoom.getModeratorId() != null ) {
            chatRoomEntity.setModeratorId( chatRoom.getModeratorId() );
        }
        chatRoomEntity.setName( chatRoom.getName() );

        return chatRoomEntity;
    }

    @Override
    public ChatRoom mapTo(ChatRoomEntity chatRoomEntity) {
        if ( chatRoomEntity == null ) {
            return null;
        }

        ChatRoom chatRoom = new ChatRoom();

        chatRoom.setChatRoomId( chatRoomEntity.getId() );
        chatRoom.setUsers( userEntityListToUserList( chatRoomEntity.getUsers() ) );
        chatRoom.setName( chatRoomEntity.getName() );
        chatRoom.setModeratorId( chatRoomEntity.getModeratorId() );

        return chatRoom;
    }

    protected List<UserEntity> userListToUserEntityList(List<User> list) {
        if ( list == null ) {
            return null;
        }

        List<UserEntity> list1 = new ArrayList<UserEntity>( list.size() );
        for ( User user : list ) {
            list1.add( map( user ) );
        }

        return list1;
    }

    protected List<User> userEntityListToUserList(List<UserEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<User> list1 = new ArrayList<User>( list.size() );
        for ( UserEntity userEntity : list ) {
            list1.add( map( userEntity ) );
        }

        return list1;
    }
}
