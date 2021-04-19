import model.document.Document;
import model.document.DocumentType;
import model.user.User;

import java.util.List;

public interface DocumentService {

    Document getDocumentByTitle(String title);

    List<Document> getAllDocuments();

    void createDocument(String title);

    void deleteDocument(String title, User user);

    void setDocumentType(String title, DocumentType documentType, User user);

}
