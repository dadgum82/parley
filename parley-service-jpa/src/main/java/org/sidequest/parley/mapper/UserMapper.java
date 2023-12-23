package org.sidequest.parley.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.sidequest.parley.entity.UserEntity;
import org.sidequest.parley.model.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    UserEntity toEntity(User user);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    User toModel(UserEntity userEntity);
}