package org.sidequest.parley.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.sidequest.parley.entity.ChatMessageEntity;
import org.sidequest.parley.model.ChatMessage;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;


@Mapper(uses = {UserMapper.class, ChatRoomMapper.class})
public interface ChatMessageMapper {

    ChatMessageMapper INSTANCE = Mappers.getMapper(ChatMessageMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "chatRoom", source = "chatRoom")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "user", source = "user")
    ChatMessageEntity toEntity(ChatMessage chatMessage);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "chatRoom", source = "chatRoom")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "user", source = "user")
    ChatMessage toModel(ChatMessageEntity chatMessageEntity);

    @AfterMapping
    default void convertUtcToLocalTime(@MappingTarget ChatMessage chatMessage, ChatMessageEntity chatMessageEntity) {
        ZoneId userZoneId = ZoneId.of(chatMessage.getUser().getTimezone());
        OffsetDateTime localTime = chatMessageEntity.getTimestamp().atZoneSameInstant(userZoneId).toOffsetDateTime();
        chatMessage.setTimestamp(localTime);
    }

    @AfterMapping
    default void convertLocalTimeToUtc(@MappingTarget ChatMessageEntity chatMessageEntity, ChatMessage chatMessage) {
        ZoneId userZoneId = ZoneId.of(chatMessage.getUser().getTimezone());
        OffsetDateTime utcTime = chatMessage.getTimestamp().atZoneSameInstant(ZoneOffset.UTC).toOffsetDateTime();
        chatMessageEntity.setTimestamp(utcTime);
    }

}