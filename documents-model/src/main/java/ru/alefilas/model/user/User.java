package ru.alefilas.model.user;

import lombok.Data;

@Data
public class User {

    private Long id;
    private String name;
    private String password;
    private String email;
    private Role role;

}
