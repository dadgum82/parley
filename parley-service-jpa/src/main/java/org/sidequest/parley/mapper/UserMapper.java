package org.sidequest.parley.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.sidequest.parley.entity.UserEntity;
import org.sidequest.parley.model.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
//
//    @Mapping(target = "id", source = "id")
//    @Mapping(target = "name", source = "name")
//    UserEntity mapTo(User user);
//
//    @Mapping(target = "id", source = "id")
//    @Mapping(target = "name", source = "name")
//    User mapTo(UserEntity userEntity);
//
//    default UserEntity map(User user) {
//        UserEntity userEntity = new UserEntity();
//        userEntity.setId(user.getId());
//        userEntity.setName(user.getName());
//        return userEntity;
//    }
//
//    default User map(UserEntity userEntity) {
//        User user = new User();
//        user.setId(userEntity.getId());
//        user.setName(userEntity.getName());
//        return user;
//    }

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    UserEntity toEntity(User user);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    User toModel(UserEntity userEntity);
}