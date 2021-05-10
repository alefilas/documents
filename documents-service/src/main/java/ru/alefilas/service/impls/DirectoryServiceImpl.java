package ru.alefilas.service.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alefilas.dto.AbstractEntityDto;
import ru.alefilas.dto.InputDirectoryDto;
import ru.alefilas.dto.OutputDirectoryDto;
import ru.alefilas.model.document.AbstractEntity;
import ru.alefilas.model.document.Directory;
import ru.alefilas.model.document.Document;
import ru.alefilas.repository.DirectoryRepository;
import ru.alefilas.service.DirectoryService;
import ru.alefilas.service.exception.DirectoryNotFoundException;
import ru.alefilas.service.mapper.DirectoryMapper;
import ru.alefilas.service.mapper.DocumentMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DirectoryServiceImpl implements DirectoryService {

    private final DirectoryRepository directoryRepository;

    @Autowired
    public DirectoryServiceImpl(DirectoryRepository directoryRepository) {
        this.directoryRepository = directoryRepository;
    }

    @Override
    @Transactional
    public OutputDirectoryDto save(InputDirectoryDto inputDir) {
        Directory directory = DirectoryMapper.dtoToModel(inputDir);

        if (directory.getId() == null) {
            directory.setCreationDate(LocalDate.now());
        }

        directoryRepository.save(directory);

        return DirectoryMapper.modelToDto(directory);
    }

    @Override
    @Transactional
    public List<AbstractEntityDto> getEntitiesByDirectoryId(Long id) {

        return directoryRepository.findEntityByDirectory(id)
                .stream()
                .map(entity -> entity.isDocument() ?
                        DocumentMapper.modelToDto((Document) entity) :
                        DirectoryMapper.modelToDto((Directory) entity))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OutputDirectoryDto getDirectoryById(Long id) {
        Directory directory = directoryRepository.findById(id)
                .orElseThrow(() -> new DirectoryNotFoundException(id));
        return DirectoryMapper.modelToDto(directory);
    }

    @Override
    public void deleteById(Long id) {
        directoryRepository.deleteById(id);
    }
}
