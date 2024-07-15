package org.sidequest.parley.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.sidequest.parley.entity.ChatRoomEntity;
import org.sidequest.parley.entity.EnrollmentEntity;
import org.sidequest.parley.model.ChatRoom;
import org.sidequest.parley.model.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(uses = {UserMapper.class, EnrollmentMapper.class})
public interface ChatRoomMapper {

    ChatRoomMapper INSTANCE = Mappers.getMapper(ChatRoomMapper.class);

    @Mappings({
            @Mapping(target = "id", source = "chatRoomId"),
            @Mapping(target = "enrollments", ignore = true) // Handled manually in the service
    })
    ChatRoomEntity toEntity(ChatRoom chatRoom);

    @Mappings({
            @Mapping(target = "chatRoomId", source = "id"),
            @Mapping(target = "users", source = "enrollments", qualifiedByName = "enrollmentsToUsers")
    })
    ChatRoom toModel(ChatRoomEntity chatRoomEntity);

    @Named("enrollmentsToUsers")
    default List<User> mapEnrollmentsToUsers(Set<EnrollmentEntity> enrollments) {
        return enrollments.stream()
                .map(EnrollmentEntity::getUser)
                .map(UserMapper.INSTANCE::toModel)
                .collect(Collectors.toList());
    }

}

//@Mapper(uses = UserMapper.class)
//public interface ChatRoomMapper {
//
//    ChatRoomMapper INSTANCE = Mappers.getMapper(ChatRoomMapper.class);
//
//    @Mapping(target = "id", source = "chatRoomId")
//    @Mapping(target = "users", source = "users")
//    @Mapping(target = "moderator", source = "moderator")
//    @Mapping(target = "name", source = "name")
//    ChatRoomEntity toEntity(ChatRoom chatRoom);
//
//    @Mapping(target = "chatRoomId", source = "id")
//    @Mapping(target = "users", source = "users")
//    @Mapping(target = "name", source = "name")
//    ChatRoom toModel(ChatRoomEntity chatRoomEntity);
//}