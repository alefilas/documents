package model.moderation;

import lombok.Data;
import model.directory.Directory;
import model.document.Document;

@Data
public class ModerationTicket {

    private Long id;

    private Directory directory;

    private Document document;
}
