package ru.alefilas.service.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alefilas.repository.DocumentRepository;
import ru.alefilas.repository.DocumentTypeRepository;
import ru.alefilas.service.DocumentService;
import ru.alefilas.dto.DocumentDto;
import ru.alefilas.service.mapper.DocumentMapper;
import ru.alefilas.model.document.*;

import java.time.LocalDate;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {
    
    private final DocumentRepository documentRepository;

    private final DocumentTypeRepository documentTypeRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, DocumentTypeRepository documentTypeRepository) {
        this.documentRepository = documentRepository;
        this.documentTypeRepository = documentTypeRepository;
    }


    @Override
    @Transactional
    public DocumentDto save(DocumentDto document) {

        document.setCreationDate(LocalDate.now());

        Document savedDocument = DocumentMapper.dtoToModel(document);
        savedDocument.addVersion(document.getCurrentVersion());

        Document doc = documentRepository.save(savedDocument);

        return DocumentMapper.modelToDto(doc);
    }

    @Override
    @Transactional
    public DocumentVersion save(DocumentVersion version, Long documentId) {
        Document document = documentRepository.findById(documentId).orElseThrow();
        document.addVersion(version);
        return version;
    }

    @Override
    @Transactional
    public DocumentDto getDocumentById(Long id) {
        Document document = documentRepository.findById(id).orElseThrow();
        return DocumentMapper.modelToDto(document);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        documentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<DocumentVersion> getAllVersionByDocumentId(Long id) {
        Document document = documentRepository.findById(id).orElseThrow();
        return document.getVersions();
    }

    @Override
    @Transactional
    public DocumentType getDocumentTypeByName(String name) {
        return documentTypeRepository.findByType(name).orElseThrow();
    }

    @Override
    @Transactional
    public List<DocumentType> getAllDocumentTypes() {
        return (List<DocumentType>) documentTypeRepository.findAll();
    }
}
