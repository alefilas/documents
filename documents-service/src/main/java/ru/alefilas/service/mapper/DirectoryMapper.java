package ru.alefilas.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alefilas.dto.InputDirectoryDto;
import ru.alefilas.repository.DirectoryRepository;
import ru.alefilas.service.DirectoryService;
import ru.alefilas.dto.OutputDirectoryDto;
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

        Long id = dto.getId() == 0 ? null : dto.getId();
        Long directoryId = dto.getDirectoryId() == 0 ? null : dto.getDirectoryId();


        directory.setId(id);
        directory.setTitle(dto.getTitle());

        if (directoryId != null) {
            directory.setParentDirectory(directoryRepository.findById(directoryId).orElseThrow(
                    () -> new DirectoryNotFoundException(directoryId)
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
