package ru.alefilas.service.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.alefilas.model.document.Directory;
import ru.alefilas.model.permit.Permit;
import ru.alefilas.model.permit.PermitType;
import ru.alefilas.model.user.Role;
import ru.alefilas.model.user.User;
import ru.alefilas.repository.PermitRepository;
import ru.alefilas.repository.UserRepository;
import ru.alefilas.service.exception.AccessDeniedException;

import java.util.List;
import java.util.Optional;


@Component
public class AccessHelper {

    private static PermitRepository permitRepository;
    private static UserRepository userRepository;

    @Autowired
    public AccessHelper(PermitRepository permitRepository, UserRepository userRepository) {
        AccessHelper.permitRepository = permitRepository;
        AccessHelper.userRepository = userRepository;
    }

    public static void createPermitsForNewDirectory(Directory directory) {
        List<User> admins = userRepository.findAllByRoleIn(List.of(Role.ROLE_ADMIN));
        List<User> users = userRepository.findAllByRoleIn(List.of(Role.ROLE_USER, Role.ROLE_MODERATOR));

        for (User admin : admins) {
            Permit permit = new Permit();
            permit.setDirectory(directory);
            permit.setUser(admin);
            permit.setPermitType(PermitType.MODERATE);

            permitRepository.save(permit);
        }

        for (User user : users) {
            Permit permit = new Permit();
            permit.setDirectory(directory);
            permit.setUser(user);
            permit.setPermitType(PermitType.READ);

            permitRepository.save(permit);
        }
    }

    public static boolean checkAccessBoolean(Directory directory, PermitType permitType) {
        checkAccess(directory, permitType);
        return true;
    }

    public static void checkAccess(Directory currentDirectory, PermitType permitType) {

        if (currentDirectory == null) {
            return;
        }

        Directory parentDirectory = currentDirectory;

        while (currentDirectory != null) {
            Optional<Permit> permitOptional;

            switch (permitType) {
                case READ:
                    permitOptional = permitRepository.findReadPermit(getCurrentUser(), currentDirectory);
                    break;
                case WRITE:
                    permitOptional = permitRepository.findWritePermit(getCurrentUser(), currentDirectory);
                    break;
                case MODERATE:
                    permitOptional = permitRepository.findModeratePermit(getCurrentUser(), currentDirectory);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + permitType);
            }

            if  (permitOptional.isPresent()) {
                break;
            }

            parentDirectory = currentDirectory;
            currentDirectory = parentDirectory.getParentDirectory();
        }

        if (currentDirectory == null) {
            throw new AccessDeniedException(parentDirectory.getId(), permitType);
        }
    }

    public static String getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
