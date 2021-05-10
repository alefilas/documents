package ru.alefilas.service;

import ru.alefilas.dto.AbstractEntityDto;
import ru.alefilas.dto.InputDirectoryDto;
import ru.alefilas.dto.OutputDirectoryDto;

import java.util.List;

public interface DirectoryService {

    OutputDirectoryDto save(InputDirectoryDto directory);

    List<AbstractEntityDto> getEntitiesByDirectoryId(Long id);

    OutputDirectoryDto getDirectoryById(Long id);

    void deleteById(Long id);

}
