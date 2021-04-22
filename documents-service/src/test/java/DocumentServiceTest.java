import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.alefilas.DocumentService;
import ru.alefilas.DocumentsDao;
import ru.alefilas.impls.DocumentServiceImpls;
import ru.alefilas.impls.DocumentsDaoJdbc;
import ru.alefilas.model.document.Directory;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.document.DocumentPriority;
import ru.alefilas.model.document.DocumentVersion;
import ru.alefilas.model.moderation.ModerationStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DocumentServiceTest {

    private DocumentService service = new DocumentServiceImpls(new DocumentsDaoJdbc());

    @Test
    public void documentTypesTest() {
        List<String> list = List.of("LETTER", "FAX", "COMMAND");
        List<String> listFromDao = service.getAllDocumentTypes();
        Assert.assertEquals(list, listFromDao);
    }




}
