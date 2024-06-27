package org.sidequest.parley.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.sidequest.parley.entity.UserEntity;
import org.sidequest.parley.helpers.TimeHelper;
import org.sidequest.parley.model.User;

import java.time.OffsetDateTime;
import java.time.ZoneId;

/**
 * UserMapper is an interface that provides methods to map between User and UserEntity objects.
 * It uses the MapStruct library to generate the implementation at compile time.
 */
@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Maps a User model object to a UserEntity object.
     *
     * @param user the User model object to be mapped
     * @return the mapped UserEntity object
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    //  @Mapping(source = "lastPostedMessageDateTime", target = "lastPostedMessageDateTime")
    @Mapping(target = "lastPostedMessageDateTime", expression = "java(convertToUtc(user.getLastPostedMessageDateTime()))")
    @Mapping(target = "timezone", source = "timezone")
    UserEntity toEntity(User user);

    /**
     * Maps a UserEntity object to a User model object.
     *
     * @param userEntity the UserEntity object to be mapped
     * @return the mapped User model object
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    //@Mapping(source = "lastPostedMessageDateTime", target = "lastPostedMessageDateTime")
    @Mapping(target = "lastPostedMessageDateTime", expression = "java(convertToLocalTime(userEntity.getLastPostedMessageDateTime(), userEntity.getTimezone()))")
    @Mapping(target = "timezone", source = "timezone")
    User toModel(UserEntity userEntity);


    @AfterMapping
    default void handleTimezone(@MappingTarget UserEntity userEntity, User user) {
        OffsetDateTime localDateTime = user.getLastPostedMessageDateTime();
        OffsetDateTime utcTime = TimeHelper.toUtc(localDateTime);
        userEntity.setLastPostedMessageDateTime(utcTime);
    }

    @AfterMapping
    default void handleTimezone(@MappingTarget User user, UserEntity userEntity) {
        OffsetDateTime utcTime = userEntity.getLastPostedMessageDateTime();
        OffsetDateTime localDateTime = TimeHelper.fromUtc(utcTime, ZoneId.of(userEntity.getTimezone()));
        user.setLastPostedMessageDateTime(localDateTime);
    }

    default OffsetDateTime convertToUtc(OffsetDateTime localDateTime) {
        return TimeHelper.toUtc(localDateTime);
    }

    default OffsetDateTime convertToLocalTime(OffsetDateTime utcTime, String timezone) {
        if (utcTime == null) {
            return null;
        }
        return TimeHelper.fromUtc(utcTime, ZoneId.of(timezone));
    }
}