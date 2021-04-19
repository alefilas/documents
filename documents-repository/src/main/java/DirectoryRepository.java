import javax.swing.text.Document;

public interface DirectoryRepository {

    Document findByTitle(String title);

    void deleteByTitle(String title);

}
