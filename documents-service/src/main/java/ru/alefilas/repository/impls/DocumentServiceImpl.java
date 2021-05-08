package ru.alefilas.repository.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alefilas.service.DocumentService;
import ru.alefilas.repository.DocumentsDao;
import ru.alefilas.dto.DirectoryDto;
import ru.alefilas.dto.DocumentDto;
import ru.alefilas.dto.EntityDto;
import ru.alefilas.service.mapper.DirectoryMapper;
import ru.alefilas.service.mapper.DocumentMapper;
import ru.alefilas.service.mapper.EntityMapper;
import ru.alefilas.model.document.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentsDao dao;

    @Autowired
    public DocumentServiceImpl(@Qualifier("documentsDaoJpa") DocumentsDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional
    public DocumentDto save(DocumentDto document) {

        document.setCreationDate(LocalDate.now());

        Document savedDocument = DocumentMapper.dtoToModel(document);
        savedDocument.addVersion(document.getCurrentVersion());

        Document doc = dao.save(savedDocument);

        return DocumentMapper.modelToDto(doc);
    }

    @Override
    @Transactional
    public DirectoryDto save(DirectoryDto directory) {
        directory.setCreationDate(LocalDate.now());
        Directory dir = dao.save(DirectoryMapper.dtoToModel(directory));
        return DirectoryMapper.modelToDto(dir);
    }

    @Override
    @Transactional
    public DocumentVersion save(DocumentVersion version, Long documentId) {
        return dao.save(version, documentId);
    }

    @Override
    @Transactional
    public List<EntityDto> getEntitiesByDirectory(DirectoryDto directory) {
        return dao.findEntityByDirectory(DirectoryMapper.dtoToModel(directory))
                .stream()
                .map(EntityMapper::fromModelToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DocumentDto getDocumentById(Long id) {
        Document document = dao.findDocumentById(id);
        return document == null ? null : DocumentMapper.modelToDto(document);
    }

    @Override
    @Transactional
    public DocumentVersion getVersionById(Long id) {
        return dao.findVersionById(id);
    }

    @Override
    @Transactional
    public DirectoryDto getDirectoryById(Long id) {
        Directory directory = dao.findDirectoryById(id);
        return directory == null ? null: DirectoryMapper.modelToDto(directory);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        dao.deleteById(id);
    }

    @Override
    @Transactional
    public List<DocumentVersion> getAllVersionByDocumentId(Long id) {
        return dao.findAllVersionByDocumentId(id);
    }

    @Override
    @Transactional
    public DocumentType getDocumentTypeByName(String name) {
        return dao.findDocumentTypeByName(name);
    }

    @Override
    @Transactional
    public List<DocumentType> getAllDocumentTypes() {
        return dao.findAllDocumentTypes();
    }
}
