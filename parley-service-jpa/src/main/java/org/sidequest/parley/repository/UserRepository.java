package org.sidequest.parley.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // This is an addition to try the mapper
public interface UserRepository extends JpaRepository<UserEntity, Long> {

}

//public interface UserRepository extends CrudRepository<UserEntity, Long> {
//
//    List<User> findByLastName(String lastName);
//
//    User  findById(long id);
//}
