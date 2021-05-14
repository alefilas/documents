package ru.alefilas.service;

import ru.alefilas.dto.documents.AbstractEntityDto;
import ru.alefilas.dto.documents.InputDirectoryDto;
import ru.alefilas.dto.documents.OutputDirectoryDto;

import java.util.List;

public interface DirectoryService {

    OutputDirectoryDto save(InputDirectoryDto directory);

    List<AbstractEntityDto> getEntitiesByDirectoryId(Long id);

    OutputDirectoryDto getDirectoryById(Long id);

    void deleteById(Long id);

}
