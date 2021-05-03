package ru.alefilas.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DirectoryDto {

    private Long id;
    private LocalDate creationDate;
    private Long directory_id;
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }
}
