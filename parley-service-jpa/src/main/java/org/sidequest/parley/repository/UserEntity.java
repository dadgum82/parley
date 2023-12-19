package org.sidequest.parley.repository;

import jakarta.persistence.*;

//import lombok.Getter;
//import lombok.Setter;
//import lombok.experimental.Accessors;
//
//@Getter
//@Setter
//@Accessors(chain = true)
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
