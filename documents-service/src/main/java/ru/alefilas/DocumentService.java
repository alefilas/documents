package ru.alefilas;

import org.w3c.dom.DocumentType;
import ru.alefilas.model.document.Directory;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.document.AbstractEntity;
import ru.alefilas.model.document.DocumentVersion;
import ru.alefilas.model.user.User;

import java.util.List;

public interface DocumentService {

    List<String> getAllDocumentTypes();

    Long save(AbstractEntity entity);

    Long save(DocumentVersion version);

    List<AbstractEntity> getEntitiesByDirectory(Directory directory);

}
