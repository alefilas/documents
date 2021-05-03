package ru.alefilas.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.alefilas.DocumentService;
import ru.alefilas.DocumentsDao;
import ru.alefilas.dto.DirectoryDto;
import ru.alefilas.dto.DocumentDto;
import ru.alefilas.mapper.DirectoryMapper;
import ru.alefilas.mapper.DocumentMapper;
import ru.alefilas.model.document.*;

import java.time.LocalDate;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentsDao dao;

    @Autowired
    public DocumentServiceImpl(@Qualifier("documentsDaoJpa") DocumentsDao dao) {
        this.dao = dao;
    }

    @Override
    public DocumentDto save(DocumentDto document) {

        document.setCreationDate(LocalDate.now());

        Document savedDocument = DocumentMapper.dtoToModel(document);
        savedDocument.addVersion(document.getCurrentVersion());

        Document doc = dao.save(savedDocument);

        return DocumentMapper.modelToDto(doc);
    }

    @Override
    public DirectoryDto save(DirectoryDto directory) {
        directory.setCreationDate(LocalDate.now());
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
    public void deleteById(Long id) {
        dao.deleteById(id);
    }

    @Override
    public List<DocumentVersion> getAllVersionByDocumentId(Long id) {
        return dao.findAllVersionByDocumentId(id);
    }

    @Override
    public DocumentType findDocumentTypeByName(String name) {
        return dao.findDocumentTypeByName(name);
    }

    @Override
    public List<DocumentType> findAllDocumentTypes() {
        return dao.findAllDocumentTypes();
    }
}
