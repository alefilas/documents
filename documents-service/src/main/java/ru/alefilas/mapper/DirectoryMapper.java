package ru.alefilas.mapper;

import ru.alefilas.DocumentService;
import ru.alefilas.UsersDao;
import ru.alefilas.dto.DirectoryDto;
import ru.alefilas.impls.DocumentServiceImpls;
import ru.alefilas.impls.DocumentsDaoJdbc;
import ru.alefilas.impls.UsersDaoJdbc;
import ru.alefilas.model.document.Directory;

public class DirectoryMapper {

    private static final DocumentService service = new DocumentServiceImpls(new DocumentsDaoJdbc());

    public static Directory dtoToModel(DirectoryDto dto) {

        Directory directory = new Directory();

        directory.setId(dto.getId());
        directory.setCreationDate(dto.getCreationDate());
        directory.setTitle(dto.getTitle());

        if (dto.getDirectory_id() != null) {
            directory.setParentDirectory(DirectoryMapper.dtoToModel(service.getDirectoryById(dto.getDirectory_id())));
        }

        return directory;
    }

    public static DirectoryDto modelToDto(Directory directory) {

        DirectoryDto dto = new DirectoryDto();

        dto.setId(directory.getId());
        dto.setCreationDate(directory.getCreationDate());
        dto.setTitle(directory.getTitle());

        if (directory.getParentDirectory() != null) {
            dto.setDirectory_id(directory.getParentDirectory().getId());
        }

        return dto;
    }

}
