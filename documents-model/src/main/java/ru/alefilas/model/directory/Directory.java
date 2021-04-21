package ru.alefilas.model.directory;

import lombok.Data;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.user.User;

import java.util.List;

@Data
public class Directory {

    private Long id;

    private String title;

    private List<Document> documents;

    private List<Directory> directories;

    private Directory parentDirectory;

    private List<User> usersCanRead;

    private List<User> usersCanWrite;

    private List<User> moderators;

    public void addDocument(Document document) {
        documents.add(document);
    }

    public void addDirectory(Directory directory) {
        directories.add(directory);
    }

    public void addUserCanRead(User user) {
        usersCanRead.add(user);
    }

    public void addUserCanWrite(User user) {
        usersCanWrite.add(user);
    }

    public void addModerator(User user) {
        moderators.add(user);
    }
}
