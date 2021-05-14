package ru.alefilas.notification.model;

import lombok.Data;

@Data
public class EmailSettings implements Settings {

    private String email;
    private String password;


}
