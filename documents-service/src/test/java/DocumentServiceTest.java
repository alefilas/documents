import org.junit.Assert;
import org.junit.Test;
import ru.alefilas.DocumentService;
import ru.alefilas.UsersDao;
import ru.alefilas.dto.DirectoryDto;
import ru.alefilas.dto.DocumentDto;
import ru.alefilas.impls.DocumentServiceImpls;
import ru.alefilas.impls.DocumentsDaoJdbc;
import ru.alefilas.impls.UsersDaoJdbc;
import ru.alefilas.mapper.DirectoryMapper;
import ru.alefilas.mapper.DocumentMapper;
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

        DocumentDto document = new DocumentDto();
        document.setDocumentPriority(DocumentPriority.HIGH.toString());
        document.setUser_id(usersDao.findById(1L).getId());
        document.setType("FAX");
        document.setCurrentVersion(version);
        document.setStatus(ModerationStatus.ON_MODERATION.toString());

        DocumentDto savedDocument = service.save(document);

        DocumentDto documentFromDb = service.getDocumentById(savedDocument.getId());

        Assert.assertEquals(savedDocument, documentFromDb);
        Assert.assertEquals(version, documentFromDb.getCurrentVersion());

        service.deleteById("entity", savedDocument.getId());

        Assert.assertNull(service.getDocumentById(savedDocument.getId()));
        Assert.assertNull(service.getVersionById(version.getId()));
    }

    @Test
    public void saveDirectoryTest() {

        DirectoryDto root = new DirectoryDto();
        root.setTitle("root");

        DirectoryDto savedRoot = service.save(root);

        DirectoryDto dir = new DirectoryDto();
        dir.setDirectory_id(savedRoot.getDirectory_id());
        dir.setTitle("dir");

        DirectoryDto savedDir = service.save(dir);

        DirectoryDto rootFromDb = service.getDirectoryById(savedRoot.getId());
        DirectoryDto dirFromDb = service.getDirectoryById(savedDir.getId());

        Assert.assertEquals(savedRoot, rootFromDb);
        Assert.assertEquals(savedDir, dirFromDb);

        service.deleteById("entity", savedRoot.getId());
        service.deleteById("entity", savedDir.getId());

        Assert.assertNull(service.getDirectoryById(savedRoot.getId()));
        Assert.assertNull(service.getDirectoryById(savedDir.getId()));
    }
}
