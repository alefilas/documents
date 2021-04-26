package ru.alefilas.impls;

import ru.alefilas.DocumentService;
import ru.alefilas.DocumentsDao;
import ru.alefilas.dto.DirectoryDto;
import ru.alefilas.dto.DocumentDto;
import ru.alefilas.mapper.DirectoryMapper;
import ru.alefilas.mapper.DocumentMapper;
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
    public DocumentDto save(DocumentDto document) {
        Document doc = dao.save(DocumentMapper.dtoToModel(document));
        doc.addVersion(doc.getCurrentVersion());
        return DocumentMapper.modelToDto(doc);
    }

    @Override
    public DirectoryDto save(DirectoryDto directory) {
        Directory dir = dao.save(DirectoryMapper.dtoToModel(directory));
        return DirectoryMapper.modelToDto(dir);
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
    public DocumentDto getDocumentById(Long id) {
        Document document = dao.findDocumentById(id);
        return document == null ? null : DocumentMapper.modelToDto(document);
    }

    @Override
    public DocumentVersion getVersionById(Long id) {
        return dao.findVersionById(id);
    }

    @Override
    public DirectoryDto getDirectoryById(Long id) {
        Directory directory = dao.findDirectoryById(id);
        return directory == null ? null: DirectoryMapper.modelToDto(directory);
    }

    @Override
    public void deleteById(String table, Long id) {
        dao.deleteById(table, id);
    }

    @Override
    public List<DocumentVersion> getAllVersionByDocumentId(Long id) {
        return dao.findAllVersionByDocumentId(id);
    }
}
