package org.sidequest.parley.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.sidequest.parley.entity.ChatMessageEntity;
import org.sidequest.parley.model.ChatMessage;

@Mapper(uses = {UserMapper.class, ChatRoomMapper.class})
public interface ChatMessageMapper {

    ChatMessageMapper INSTANCE = Mappers.getMapper(ChatMessageMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "chatRoom", source = "chatRoom")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "timestamp", source = "timestamp")
    @Mapping(target = "user", source = "user")
    ChatMessageEntity toEntity(ChatMessage chatMessage);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "chatRoom", source = "chatRoom")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "timestamp", source = "timestamp")
    @Mapping(target = "user", source = "user")
    ChatMessage toModel(ChatMessageEntity chatMessageEntity);
}