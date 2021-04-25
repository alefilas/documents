package ru.alefilas.impls;

import ru.alefilas.DocumentService;
import ru.alefilas.DocumentsDao;
import ru.alefilas.model.document.AbstractEntity;
import ru.alefilas.model.document.Directory;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.document.DocumentVersion;

import java.util.List;

public class DocumentServiceImpls implements DocumentService {

    private final DocumentsDao dao;

    public DocumentServiceImpls(DocumentsDao dao) {
        this.dao = dao;
    }

    @Override
    public Document save(Document document) {
        Document doc = dao.save(document);
        doc.addVersion(doc.getCurrentVersion());
        return doc;
    }

    @Override
    public Directory save(Directory directory) {
        return dao.save(directory);
    }

    @Override
    public DocumentVersion save(DocumentVersion version, Long documentId) {
        return dao.save(version, documentId);
    }

    @Override
    public List<AbstractEntity> getEntitiesByDirectory(Directory directory) {
        return dao.findEntityByDirectory(directory);
    }

    @Override
    public Document findDocumentById(Long id) {
        return dao.findDocumentById(id);
    }

    @Override
    public DocumentVersion findVersionById(Long id) {
        return dao.findVersionById(id);
    }

    @Override
    public Directory findDirectoryById(Long id) {
        return dao.findDirectoryById(id);
    }

    @Override
    public void deleteById(String table, Long id) {
        dao.deleteById(table, id);
    }

    @Override
    public List<DocumentVersion> findAllVersionByDocumentId(Long id) {
        return dao.findAllVersionByDocumentId(id);
    }
}
