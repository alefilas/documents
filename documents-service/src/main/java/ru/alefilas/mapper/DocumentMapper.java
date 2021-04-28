package ru.alefilas.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alefilas.DocumentService;
import ru.alefilas.UsersDao;
import ru.alefilas.dto.DocumentDto;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.document.DocumentPriority;
import ru.alefilas.model.moderation.ModerationStatus;

@Component
public class DocumentMapper {

    private static DocumentService service;

    private static UsersDao usersDao;

    @Autowired
    public DocumentMapper(DocumentService service, UsersDao usersDao) {
        DocumentMapper.service = service;
        DocumentMapper.usersDao = usersDao;
    }

    public static Document dtoToModel(DocumentDto dto) {

        Document document = new Document();

        document.setId(dto.getId());
        document.setCreationDate(dto.getCreationDate());
        document.setCurrentVersion(dto.getCurrentVersion());
        document.setDocumentPriority(DocumentPriority.valueOf(dto.getDocumentPriority()));
        document.setUser(usersDao.findById(dto.getUser_id()));
        document.setType(dto.getType());
        document.setStatus(ModerationStatus.valueOf(dto.getStatus()));

        if(dto.getId() != null) {
            document.setVersions(service.getAllVersionByDocumentId(dto.getId()));
        }

        if (dto.getDirectory_id() != null) {
            document.setParentDirectory(DirectoryMapper.dtoToModel(service.getDirectoryById(dto.getDirectory_id())));
        }

        return document;
    }

    public static DocumentDto modelToDto(Document document) {

        DocumentDto dto = new DocumentDto();

        dto.setId(document.getId());
        dto.setCreationDate(document.getCreationDate());
        dto.setCurrentVersion(document.getCurrentVersion());
        dto.setDocumentPriority(document.getDocumentPriority().toString());
        dto.setUser_id(document.getUser().getId());
        dto.setType(document.getType());
        dto.setStatus(dto.getStatus());

        if (document.getParentDirectory() != null) {
            dto.setDirectory_id(DirectoryMapper.dtoToModel(service.getDirectoryById(dto.getDirectory_id())).getId());
        }

        return dto;
    }

}
