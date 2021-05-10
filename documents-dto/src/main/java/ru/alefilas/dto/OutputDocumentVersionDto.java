package ru.alefilas.dto;

import lombok.Data;
import ru.alefilas.model.moderation.ModerationStatus;

import java.util.List;

@Data
public class OutputDocumentVersionDto {

    private String title;
    private String description;
    private List<String> files;
    private ModerationStatus status;

}
