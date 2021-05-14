package ru.alefilas.dto.documents;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.alefilas.model.document.DocumentPriority;
import ru.alefilas.model.moderation.ModerationStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class OutputDocumentDto extends AbstractEntityDto {

    private OutputDocumentVersionDto currentVersion;
    private DocumentPriority documentPriority;
    private String username;
    private String type;
    private ModerationStatus status;
}
