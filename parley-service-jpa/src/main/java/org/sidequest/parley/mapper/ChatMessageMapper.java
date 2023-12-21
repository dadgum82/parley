package org.sidequest.parley.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.sidequest.parley.model.ChatMessage;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.ChatMessageEntity;

import java.util.Optional;

@Mapper
public interface ChatMessageMapper {

    public static ChatMessageMapper INSTANCE = Mappers.getMapper(ChatMessageMapper.class);

    //    @Mappings({
//            @Mapping(target = "id", source = "id"),
//            @Mapping(target = "chatRoomId", source = "chatRoomId"),
//            @Mapping(target = "content", source = "content"),
//            @Mapping(target = "timestamp", source = "timestamp"),
//            @Mapping(target = "user", source = "user")
//    })
    ChatMessageEntity mapTo(ChatMessage chatMessage);

    @InheritInverseConfiguration
    ChatMessage mapTo(ChatMessageEntity chatMessageEntity);

    @InheritInverseConfiguration
    User mapTo(Optional<ChatMessageEntity> chatMessageEntity);
}