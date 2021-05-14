package ru.alefilas.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alefilas.dto.permit.PermitDto;
import ru.alefilas.model.document.Directory;
import ru.alefilas.model.permit.Permit;
import ru.alefilas.model.user.User;
import ru.alefilas.repository.DirectoryRepository;
import ru.alefilas.repository.UserRepository;
import ru.alefilas.service.exception.DirectoryNotFoundException;
import ru.alefilas.service.exception.UserNotFoundException;

@Component
public class PermitMapper {

    private static DirectoryRepository directoryRepository;

    private static UserRepository userRepository;

    @Autowired
    public PermitMapper(DirectoryRepository directoryRepository, UserRepository userRepository) {
        PermitMapper.directoryRepository = directoryRepository;
        PermitMapper.userRepository = userRepository;
    }

    public static PermitDto modelToDto(Permit permit) {
        PermitDto permitDto = new PermitDto();
        permitDto.setPermitType(permit.getPermitType());
        permitDto.setUsername(permit.getUser().getUsername());
        permitDto.setDirectoryId(permit.getDirectory().getId());
        return permitDto;
    }

    public static Permit dtoToModel(PermitDto permitDto) {
        Permit permit = new Permit();
        permit.setPermitType(permitDto.getPermitType());

        User user = userRepository.findByUsername(permitDto.getUsername()).orElseThrow(
                () -> new UserNotFoundException(permitDto.getUsername())
        );

        Directory directory = directoryRepository.findById(permitDto.getDirectoryId()).orElseThrow(
                () -> new DirectoryNotFoundException(permitDto.getDirectoryId())
        );

        permit.setUser(user);
        permit.setDirectory(directory);

        return permit;
    }

}
