import javax.swing.text.Document;

public interface DocumentRepository {

    Document findByTitle(String title);

    void deleteByTitle(String title);


}
