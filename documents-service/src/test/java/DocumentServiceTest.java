import org.junit.Assert;
import org.junit.Test;
import ru.alefilas.DocumentService;
import ru.alefilas.UsersDao;
import ru.alefilas.impls.DocumentServiceImpls;
import ru.alefilas.impls.DocumentsDaoJdbc;
import ru.alefilas.impls.UsersDaoJdbc;
import ru.alefilas.model.document.Directory;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.document.DocumentPriority;
import ru.alefilas.model.document.DocumentVersion;
import ru.alefilas.model.moderation.ModerationStatus;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DocumentServiceTest {

    private final DocumentService service = new DocumentServiceImpls(new DocumentsDaoJdbc());
    private final UsersDao usersDao = new UsersDaoJdbc();

    @Test
    public void saveDocumentTest() {

        DocumentVersion version = new DocumentVersion();
        version.setTitle("title");
        version.setDescription("desc");
        version.setFiles(List.of(Path.of("C:\\Users\\safil\\Desktop\\javaCourse\\diagram.png"),
                Path.of("C:\\Users\\safil\\Desktop\\javaCourse\\diagram2.png")));
        version.setStatus(ModerationStatus.ON_MODERATION);

        Document document = new Document();
        document.setDocumentPriority(DocumentPriority.HIGH);
        document.setUser(usersDao.findById(1L));
        document.setType("FAX");
        document.setCurrentVersion(version);
        document.setStatus(ModerationStatus.ON_MODERATION);
        document.setVersions(new ArrayList<>());

        service.save(document);

        Document documentFromDb = service.findDocumentById(document.getId());

        Assert.assertEquals(document.getParentDirectory(), documentFromDb.getParentDirectory());
        Assert.assertEquals(document, documentFromDb);
        Assert.assertEquals(version, documentFromDb.getCurrentVersion());

        service.deleteById("entity", document.getId());

        Assert.assertNull(service.findDocumentById(document.getId()));
        Assert.assertNull(service.findVersionById(version.getId()));
    }

    @Test
    public void saveDirectoryTest() {

        Directory root = new Directory();
        root.setTitle("root");

        Directory dir = new Directory();
        dir.setParentDirectory(root);
        dir.setTitle("dir");

        dir.setParentDirectory(root);

        service.save(root);
        service.save(dir);

        Directory rootFromDb = service.findDirectoryById(root.getId());
        Directory dirFromDb = service.findDirectoryById(dir.getId());

        Assert.assertEquals(root, rootFromDb);
        Assert.assertEquals(dir, dirFromDb);

        service.deleteById("entity", root.getId());
        service.deleteById("entity", dir.getId());

        Assert.assertNull(service.findDocumentById(root.getId()));
        Assert.assertNull(service.findVersionById(dir.getId()));
    }
}
