package ru.alefilas;


import ru.alefilas.model.document.AbstractEntity;
import ru.alefilas.model.document.Directory;
import ru.alefilas.model.document.DocumentVersion;

import java.util.List;

public interface DocumentsDao {

    List<String> findAllDocumentTypes();

    Long save(AbstractEntity entity);

    Long save(DocumentVersion version);

    List<AbstractEntity> findEntityByDirectory(Directory directory);

    void deleteById(String table, Long id);

}
