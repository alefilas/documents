package ru.alefilas.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AbstractEntityDto {

    private Long id;
    private LocalDate creationDate;
    private Long directoryId;

}
