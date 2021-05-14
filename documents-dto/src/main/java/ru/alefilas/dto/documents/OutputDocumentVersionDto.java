package ru.alefilas.dto.documents;

import lombok.Data;

import java.util.List;

@Data
public class OutputDocumentVersionDto {

    private Long id;
    private String title;
    private String description;
    private List<String> files;

}
