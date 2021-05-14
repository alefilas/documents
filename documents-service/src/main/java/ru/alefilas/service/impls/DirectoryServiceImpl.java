package ru.alefilas.service.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alefilas.dto.documents.AbstractEntityDto;
import ru.alefilas.dto.documents.InputDirectoryDto;
import ru.alefilas.dto.documents.OutputDirectoryDto;
import ru.alefilas.model.document.Directory;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.permit.PermitType;
import ru.alefilas.repository.DirectoryRepository;
import ru.alefilas.repository.PermitRepository;
import ru.alefilas.service.DirectoryService;
import ru.alefilas.service.access.AccessHelper;
import ru.alefilas.service.exception.DirectoryNotFoundException;
import ru.alefilas.service.mapper.DirectoryMapper;
import ru.alefilas.service.mapper.DocumentMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DirectoryServiceImpl implements DirectoryService {

    @Autowired
    private DirectoryRepository directoryRepository;

    @Override
    @Transactional
    public OutputDirectoryDto save(InputDirectoryDto inputDir) {
        Directory directory = DirectoryMapper.dtoToModel(inputDir);

        AccessHelper.checkAccess(directory.getParentDirectory(), PermitType.WRITE);

        if (directory.getId() == null) {
            directory.setCreationDate(LocalDate.now());
        }

        directoryRepository.save(directory);

        AccessHelper.createPermitsForNewDirectory(directory);

        return DirectoryMapper.modelToDto(directory);
    }

    @Override
    @Transactional
    public List<AbstractEntityDto> getEntitiesByDirectoryId(Long id) {

        Directory directory = findDirectoryById(id);

        AccessHelper.checkAccess(directory.getParentDirectory(), PermitType.READ);

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
        Directory directory = findDirectoryById(id);
        AccessHelper.checkAccess(directory.getParentDirectory(), PermitType.READ);
        return DirectoryMapper.modelToDto(directory);
    }

    @Override
    public void deleteById(Long id) {
        Directory directory = findDirectoryById(id);
        AccessHelper.checkAccess(directory.getParentDirectory(), PermitType.WRITE);
        directoryRepository.delete(directory);
    }

    private Directory findDirectoryById(Long id) {
        return directoryRepository.findById(id).orElseThrow(
                () -> new DirectoryNotFoundException(id)
        );
    }
}
