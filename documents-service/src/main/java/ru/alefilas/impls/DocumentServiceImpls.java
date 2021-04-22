package ru.alefilas.impls;

import ru.alefilas.DocumentService;
import ru.alefilas.DocumentsDao;
import ru.alefilas.model.document.AbstractEntity;
import ru.alefilas.model.document.Directory;
import ru.alefilas.model.document.DocumentVersion;

import java.util.List;

public class DocumentServiceImpls implements DocumentService {

    private DocumentsDao dao;

    public DocumentServiceImpls(DocumentsDao dao) {
        this.dao = dao;
    }

    @Override
    public List<String> getAllDocumentTypes() {
        return dao.findAllDocumentTypes();
    }

    @Override
    public Long save(AbstractEntity entity) {
        return dao.save(entity);
    }

    @Override
    public Long save(DocumentVersion version) {
        return dao.save(version);
    }

    @Override
    public List<AbstractEntity> getEntitiesByDirectory(Directory directory) {
        return dao.findEntityByDirectory(directory);
    }
}
