package ru.alefilas.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alefilas.service.DocumentService;
import ru.alefilas.dto.DirectoryDto;
import ru.alefilas.model.document.Directory;

@Component
public class DirectoryMapper {

    private static DocumentService service;

    @Autowired
    public DirectoryMapper(DocumentService service) {
        DirectoryMapper.service = service;
    }

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
