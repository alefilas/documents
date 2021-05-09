package ru.alefilas.service.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alefilas.dto.DirectoryDto;
import ru.alefilas.dto.EntityDto;
import ru.alefilas.model.document.Directory;
import ru.alefilas.repository.DirectoryRepository;
import ru.alefilas.service.DirectoryService;
import ru.alefilas.service.mapper.DirectoryMapper;
import ru.alefilas.service.mapper.EntityMapper;

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
    public DirectoryDto save(DirectoryDto directory) {
        directory.setCreationDate(LocalDate.now());
        Directory dir = directoryRepository.save(DirectoryMapper.dtoToModel(directory));
        return DirectoryMapper.modelToDto(dir);
    }

    @Override
    @Transactional
    public List<EntityDto> getEntitiesByDirectory(DirectoryDto directory) {
        return directoryRepository.findEntityByDirectory(DirectoryMapper.dtoToModel(directory))
                .stream()
                .map(EntityMapper::fromModelToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DirectoryDto getDirectoryById(Long id) {
        Directory directory = directoryRepository.findById(id).orElseThrow();
        return DirectoryMapper.modelToDto(directory);
    }

    @Override
    public void deleteById(Long id) {
        Directory directory = directoryRepository.findById(id).orElseThrow();
        directoryRepository.delete(directory);
    }
}
