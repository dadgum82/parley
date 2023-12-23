package org.sidequest.parley.mapper;

import javax.annotation.processing.Generated;
import org.sidequest.parley.entity.ChatMessageEntity;
import org.sidequest.parley.entity.ChatRoomEntity;
import org.sidequest.parley.model.ChatMessage;
import org.sidequest.parley.model.ChatRoom;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-23T15:30:26-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
public class ChatMessageMapperImpl implements ChatMessageMapper {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public ChatMessageEntity toEntity(ChatMessage chatMessage) {
        if ( chatMessage == null ) {
            return null;
        }

        ChatMessageEntity chatMessageEntity = new ChatMessageEntity();

        if ( chatMessage.getId() != null ) {
            chatMessageEntity.setId( chatMessage.getId().intValue() );
        }
        chatMessageEntity.setChatRoom( chatRoomToChatRoomEntity( chatMessage.getChatRoom() ) );
        chatMessageEntity.setContent( chatMessage.getContent() );
        chatMessageEntity.setTimestamp( chatMessage.getTimestamp() );
        chatMessageEntity.setUser( userMapper.toEntity( chatMessage.getUser() ) );

        return chatMessageEntity;
    }

    @Override
    public ChatMessage toModel(ChatMessageEntity chatMessageEntity) {
        if ( chatMessageEntity == null ) {
            return null;
        }

        ChatMessage chatMessage = new ChatMessage();

        chatMessage.setId( (long) chatMessageEntity.getId() );
        chatMessage.setChatRoom( chatRoomEntityToChatRoom( chatMessageEntity.getChatRoom() ) );
        chatMessage.setContent( chatMessageEntity.getContent() );
        chatMessage.setTimestamp( chatMessageEntity.getTimestamp() );
        chatMessage.setUser( userMapper.toModel( chatMessageEntity.getUser() ) );

        return chatMessage;
    }

    protected ChatRoomEntity chatRoomToChatRoomEntity(ChatRoom chatRoom) {
        if ( chatRoom == null ) {
            return null;
        }

        ChatRoomEntity chatRoomEntity = new ChatRoomEntity();

        chatRoomEntity.setModerator( userMapper.toEntity( chatRoom.getModerator() ) );
        chatRoomEntity.setName( chatRoom.getName() );

        return chatRoomEntity;
    }

    protected ChatRoom chatRoomEntityToChatRoom(ChatRoomEntity chatRoomEntity) {
        if ( chatRoomEntity == null ) {
            return null;
        }

        ChatRoom chatRoom = new ChatRoom();

        chatRoom.setName( chatRoomEntity.getName() );
        chatRoom.setModerator( userMapper.toModel( chatRoomEntity.getModerator() ) );

        return chatRoom;
    }
}
