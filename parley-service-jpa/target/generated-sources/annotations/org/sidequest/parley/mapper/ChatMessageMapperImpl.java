package org.sidequest.parley.mapper;

import java.util.Optional;
import javax.annotation.processing.Generated;
import org.sidequest.parley.model.ChatMessage;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.ChatMessageEntity;
import org.sidequest.parley.repository.UserEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-22T14:01:47-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
public class ChatMessageMapperImpl implements ChatMessageMapper {

    @Override
    public ChatMessageEntity mapTo(ChatMessage chatMessage) {
        if ( chatMessage == null ) {
            return null;
        }

        ChatMessageEntity chatMessageEntity = new ChatMessageEntity();

        if ( chatMessage.getId() != null ) {
            chatMessageEntity.setId( chatMessage.getId() );
        }
        if ( chatMessage.getChatRoomId() != null ) {
            chatMessageEntity.setChatRoomId( chatMessage.getChatRoomId() );
        }
        chatMessageEntity.setContent( chatMessage.getContent() );
        chatMessageEntity.setTimestamp( chatMessage.getTimestamp() );
        chatMessageEntity.setUser( userToUserEntity( chatMessage.getUser() ) );

        return chatMessageEntity;
    }

    @Override
    public ChatMessage mapTo(ChatMessageEntity chatMessageEntity) {
        if ( chatMessageEntity == null ) {
            return null;
        }

        ChatMessage chatMessage = new ChatMessage();

        chatMessage.setId( chatMessageEntity.getId() );
        chatMessage.setChatRoomId( chatMessageEntity.getChatRoomId() );
        chatMessage.setContent( chatMessageEntity.getContent() );
        chatMessage.setTimestamp( chatMessageEntity.getTimestamp() );
        chatMessage.setUser( userEntityToUser( chatMessageEntity.getUser() ) );

        return chatMessage;
    }

    @Override
    public User mapTo(Optional<ChatMessageEntity> chatMessageEntity) {
        if ( chatMessageEntity == null ) {
            return null;
        }

        User user = new User();

        return user;
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

    protected User userEntityToUser(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        User user = new User();

        user.setId( userEntity.getId() );
        user.setName( userEntity.getName() );

        return user;
    }
}
