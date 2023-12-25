package org.sidequest.parley.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.sidequest.parley.entity.ChatRoomEntity;
import org.sidequest.parley.entity.ChatRoomUsersEntity;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-25T12:56:19-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
public class ChatRoomMapperImpl implements ChatRoomMapper {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public ChatRoomEntity toEntity(ChatRoom chatRoom) {
        if ( chatRoom == null ) {
            return null;
        }

        ChatRoomEntity chatRoomEntity = new ChatRoomEntity();

        chatRoomEntity.setId( chatRoom.getChatRoomId() );
        chatRoomEntity.setChatRoomUsers( userListToChatRoomUsersEntityList( chatRoom.getUsers() ) );
        chatRoomEntity.setModerator( userMapper.toEntity( chatRoom.getModerator() ) );
        chatRoomEntity.setName( chatRoom.getName() );

        return chatRoomEntity;
    }

    @Override
    public ChatRoom toModel(ChatRoomEntity chatRoomEntity) {
        if ( chatRoomEntity == null ) {
            return null;
        }

        ChatRoom chatRoom = new ChatRoom();

        chatRoom.setChatRoomId( chatRoomEntity.getId() );
        chatRoom.setUsers( chatRoomUsersEntityListToUserList( chatRoomEntity.getChatRoomUsers() ) );
        chatRoom.setName( chatRoomEntity.getName() );
        chatRoom.setModerator( userMapper.toModel( chatRoomEntity.getModerator() ) );

        return chatRoom;
    }

    protected ChatRoomUsersEntity userToChatRoomUsersEntity(User user) {
        if ( user == null ) {
            return null;
        }

        ChatRoomUsersEntity chatRoomUsersEntity = new ChatRoomUsersEntity();

        chatRoomUsersEntity.setId( user.getId() );

        return chatRoomUsersEntity;
    }

    protected List<ChatRoomUsersEntity> userListToChatRoomUsersEntityList(List<User> list) {
        if ( list == null ) {
            return null;
        }

        List<ChatRoomUsersEntity> list1 = new ArrayList<ChatRoomUsersEntity>( list.size() );
        for ( User user : list ) {
            list1.add( userToChatRoomUsersEntity( user ) );
        }

        return list1;
    }

    protected User chatRoomUsersEntityToUser(ChatRoomUsersEntity chatRoomUsersEntity) {
        if ( chatRoomUsersEntity == null ) {
            return null;
        }

        User user = new User();

        user.setId( chatRoomUsersEntity.getId() );

        return user;
    }

    protected List<User> chatRoomUsersEntityListToUserList(List<ChatRoomUsersEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<User> list1 = new ArrayList<User>( list.size() );
        for ( ChatRoomUsersEntity chatRoomUsersEntity : list ) {
            list1.add( chatRoomUsersEntityToUser( chatRoomUsersEntity ) );
        }

        return list1;
    }
}
