package ru.alefilas.dto;

import lombok.Data;
import ru.alefilas.model.document.DocumentVersion;

import java.time.LocalDate;
import java.util.Objects;

@Data
public class DocumentDto {

    private Long id;
    private LocalDate creationDate;
    private Long directory_id;
    private DocumentVersion currentVersion;
    private String documentPriority;
    private Long user_id;
    private String type;
    private String status;
}
