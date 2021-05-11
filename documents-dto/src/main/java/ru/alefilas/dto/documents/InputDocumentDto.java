package ru.alefilas.dto.documents;

import lombok.Data;
import ru.alefilas.model.document.DocumentPriority;

@Data
public class InputDocumentDto {

    private Long id;
    private Long directoryId;
    private InputDocumentVersionDto currentVersion;
    private DocumentPriority documentPriority;
    private String username;
    private String type;

}
