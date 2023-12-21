package org.sidequest.parley.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.repository.ChatRoomEntity;
import org.sidequest.parley.repository.ChatRoomUsersEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-21T16:41:46-0500",
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
        chatRoomEntity.setUserIds( integerListToChatRoomUsersEntityList( chatRoom.getUserIds() ) );
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
        chatRoom.setUserIds( chatRoomUsersEntityListToIntegerList( chatRoomEntity.getUserIds() ) );
        chatRoom.setName( chatRoomEntity.getName() );
        chatRoom.setModeratorId( chatRoomEntity.getModeratorId() );

        return chatRoom;
    }

    protected List<ChatRoomUsersEntity> integerListToChatRoomUsersEntityList(List<Integer> list) {
        if ( list == null ) {
            return null;
        }

        List<ChatRoomUsersEntity> list1 = new ArrayList<ChatRoomUsersEntity>( list.size() );
        for ( Integer integer : list ) {
            list1.add( map( integer ) );
        }

        return list1;
    }

    protected List<Integer> chatRoomUsersEntityListToIntegerList(List<ChatRoomUsersEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<Integer> list1 = new ArrayList<Integer>( list.size() );
        for ( ChatRoomUsersEntity chatRoomUsersEntity : list ) {
            list1.add( map( chatRoomUsersEntity ) );
        }

        return list1;
    }
}
