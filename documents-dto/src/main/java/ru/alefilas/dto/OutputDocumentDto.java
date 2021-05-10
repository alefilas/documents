package ru.alefilas.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.alefilas.model.document.DocumentPriority;
import ru.alefilas.model.document.DocumentVersion;
import ru.alefilas.model.moderation.ModerationStatus;

import java.time.LocalDate;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class OutputDocumentDto extends AbstractEntityDto {

    private OutputDocumentVersionDto currentVersion;
    private DocumentPriority documentPriority;
    private String username;
    private String type;
    private ModerationStatus status;
}
