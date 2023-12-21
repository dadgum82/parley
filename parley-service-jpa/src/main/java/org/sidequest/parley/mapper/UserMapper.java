package org.sidequest.parley.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.UserEntity;

import java.util.Optional;

@Mapper
public interface UserMapper {

    public static UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    //    @Mappings({
//            @Mapping(target = "id", source = "id"),
//            @Mapping(target = "name", source = "name")
//    })
    UserEntity mapTo(User user);

    @InheritInverseConfiguration
    User mapTo(UserEntity userEntity);

    @InheritInverseConfiguration
    User mapTo(Optional<UserEntity> userEntity);
}