package org.sidequest.parley.mapper;

import java.util.Optional;
import javax.annotation.processing.Generated;
import org.sidequest.parley.model.User;
import org.sidequest.parley.repository.UserEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-21T16:26:10-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserEntity mapTo(User user) {
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

    @Override
    public User mapTo(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        User user = new User();

        user.setId( userEntity.getId() );
        user.setName( userEntity.getName() );

        return user;
    }

    @Override
    public User mapTo(Optional<UserEntity> userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        User user = new User();

        return user;
    }
}
