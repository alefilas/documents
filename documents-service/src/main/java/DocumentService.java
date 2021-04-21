import ru.alefilas.model.document.Document;
import ru.alefilas.model.document.DocumentType;
import ru.alefilas.model.user.User;

import java.util.List;

public interface DocumentService {

    Document getDocumentByTitle(String title);

    List<Document> getAllDocuments();

    void createDocument(String title);

    void deleteDocument(String title, User user);

    void setDocumentType(String title, DocumentType documentType, User user);

}
