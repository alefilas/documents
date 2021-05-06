package ru.alefilas.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EntityDto {

    private Long id;
    private LocalDate creationDate;
    private Long directoryId;
    private String title;
    private String type;

}
