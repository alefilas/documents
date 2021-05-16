package ru.alefilas.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alefilas.dto.documents.InputDirectoryDto;
import ru.alefilas.repository.DirectoryRepository;
import ru.alefilas.dto.documents.OutputDirectoryDto;
import ru.alefilas.model.document.Directory;
import ru.alefilas.service.exception.DirectoryNotFoundException;

@Component
public class DirectoryMapper {

    private static DirectoryRepository directoryRepository;

    @Autowired
    public DirectoryMapper(DirectoryRepository directoryRepository) {
        DirectoryMapper.directoryRepository = directoryRepository;
    }

    public static Directory dtoToModel(InputDirectoryDto dto) {

        Directory directory = new Directory();

        if (dto.getId() != null && dto.getId() != 0) {
            directory.setId(dto.getId());
        }

        directory.setTitle(dto.getTitle());

        if (dto.getDirectoryId() != null && dto.getDirectoryId() != 0) {
            directory.setParentDirectory(directoryRepository.findById(dto.getDirectoryId()).orElseThrow(
                    () -> new DirectoryNotFoundException(dto.getDirectoryId())
            ));
        }

        return directory;
    }

    public static OutputDirectoryDto modelToDto(Directory directory) {

        OutputDirectoryDto dto = new OutputDirectoryDto();

        dto.setId(directory.getId());
        dto.setCreationDate(directory.getCreationDate());
        dto.setTitle(directory.getTitle());

        if (directory.getParentDirectory() != null) {
            dto.setDirectoryId(directory.getParentDirectory().getId());
        }

        return dto;
    }

}
