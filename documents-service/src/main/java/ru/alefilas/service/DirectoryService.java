package ru.alefilas.service;

import ru.alefilas.dto.DirectoryDto;
import ru.alefilas.dto.EntityDto;

import java.util.List;

public interface DirectoryService {

    DirectoryDto save(DirectoryDto directory);

    List<EntityDto> getEntitiesByDirectory(DirectoryDto directory);

    DirectoryDto getDirectoryById(Long id);

    void deleteById(Long id);

}
