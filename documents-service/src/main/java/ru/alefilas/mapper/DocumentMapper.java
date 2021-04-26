package ru.alefilas.mapper;

import ru.alefilas.DocumentService;
import ru.alefilas.UsersDao;
import ru.alefilas.dto.DirectoryDto;
import ru.alefilas.dto.DocumentDto;
import ru.alefilas.impls.DocumentServiceImpls;
import ru.alefilas.impls.DocumentsDaoJdbc;
import ru.alefilas.impls.UsersDaoJdbc;
import ru.alefilas.model.document.Directory;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.document.DocumentPriority;
import ru.alefilas.model.moderation.ModerationStatus;

import java.util.ArrayList;
import java.util.List;

public class DocumentMapper {

    private static final DocumentService service = new DocumentServiceImpls(new DocumentsDaoJdbc());
    private static final UsersDao usersDao = new UsersDaoJdbc();

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
