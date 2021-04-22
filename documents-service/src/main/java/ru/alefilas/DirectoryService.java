package ru.alefilas;

import ru.alefilas.model.user.User;

public interface DirectoryService {

    void addUserCanRead(User user, String title);

    void addUserCanWrite(User user, String title);

    void deleteUserCanRead(User user, String title);

    void deleteUserCanWrite(User user, String title);

    void createDirectory(String title);

    void deleteDirectory(String title, User user);

    void addModerator(User user);

}
