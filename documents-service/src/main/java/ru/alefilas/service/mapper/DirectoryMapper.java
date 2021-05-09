package ru.alefilas.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alefilas.service.DirectoryService;
import ru.alefilas.service.DocumentService;
import ru.alefilas.dto.DirectoryDto;
import ru.alefilas.model.document.Directory;

@Component
public class DirectoryMapper {

    private static DirectoryService directoryService;

    @Autowired
    public DirectoryMapper(DirectoryService directoryService) {
        DirectoryMapper.directoryService = directoryService;
    }

    public static Directory dtoToModel(DirectoryDto dto) {

        Directory directory = new Directory();

        directory.setId(dto.getId());
        directory.setCreationDate(dto.getCreationDate());
        directory.setTitle(dto.getTitle());

        if (dto.getDirectoryId() != null) {
            directory.setParentDirectory(DirectoryMapper.dtoToModel(directoryService.getDirectoryById(dto.getDirectoryId())));
        }

        return directory;
    }

    public static DirectoryDto modelToDto(Directory directory) {

        DirectoryDto dto = new DirectoryDto();

        dto.setId(directory.getId());
        dto.setCreationDate(directory.getCreationDate());
        dto.setTitle(directory.getTitle());

        if (directory.getParentDirectory() != null) {
            dto.setDirectoryId(directory.getParentDirectory().getId());
        }

        return dto;
    }

}
