package ru.alefilas.dto.documents;

import lombok.Data;

import java.util.List;

@Data
public class InputDocumentVersionDto {

    private String title;
    private String description;
    private List<String> files;

}
