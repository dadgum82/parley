package org.sidequest.parley.mapper;

import javax.annotation.processing.Generated;
import org.sidequest.parley.entity.ChatMessageEntity;
import org.sidequest.parley.model.ChatMessage;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-25T12:56:20-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
public class ChatMessageMapperImpl implements ChatMessageMapper {

    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final ChatRoomMapper chatRoomMapper = ChatRoomMapper.INSTANCE;

    @Override
    public ChatMessageEntity toEntity(ChatMessage chatMessage) {
        if ( chatMessage == null ) {
            return null;
        }

        ChatMessageEntity chatMessageEntity = new ChatMessageEntity();

        chatMessageEntity.setId( chatMessage.getId() );
        chatMessageEntity.setChatRoom( chatRoomMapper.toEntity( chatMessage.getChatRoom() ) );
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

        chatMessage.setId( chatMessageEntity.getId() );
        chatMessage.setChatRoom( chatRoomMapper.toModel( chatMessageEntity.getChatRoom() ) );
        chatMessage.setContent( chatMessageEntity.getContent() );
        chatMessage.setTimestamp( chatMessageEntity.getTimestamp() );
        chatMessage.setUser( userMapper.toModel( chatMessageEntity.getUser() ) );

        return chatMessage;
    }
}
