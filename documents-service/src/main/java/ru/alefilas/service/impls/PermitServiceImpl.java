package ru.alefilas.service.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alefilas.dto.permit.PermitDto;
import ru.alefilas.model.permit.Permit;
import ru.alefilas.repository.PermitRepository;
import ru.alefilas.service.PermitService;
import ru.alefilas.service.mapper.PermitMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermitServiceImpl implements PermitService {

    @Autowired
    private PermitRepository permitRepository;

    @Override
    public PermitDto save(PermitDto dto) {

        Permit permit = PermitMapper.dtoToModel(dto);
        Optional<Permit> optionalPermit =
                permitRepository.findByDirectoryAndUser(permit.getDirectory(), permit.getUser());

        if (optionalPermit.isPresent()) {
            permit = optionalPermit.get();
            permit.setPermitType(dto.getPermitType());
        }

        permit = permitRepository.save(permit);

        return PermitMapper.modelToDto(permit);
    }

    @Override
    public void delete(Long id) {
        permitRepository.deleteById(id);
    }

    @Override
    public List<PermitDto> getAllPermits() {
        return permitRepository.findAll()
                .stream()
                .map(PermitMapper::modelToDto)
                .collect(Collectors.toList());
    }
}
