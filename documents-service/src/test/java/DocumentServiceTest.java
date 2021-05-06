import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.alefilas.DocumentService;
import ru.alefilas.UsersDao;
import ru.alefilas.config.ServiceConfig;
import ru.alefilas.dto.DirectoryDto;
import ru.alefilas.dto.DocumentDto;
import ru.alefilas.model.document.DocumentPriority;
import ru.alefilas.model.document.DocumentType;
import ru.alefilas.model.document.DocumentVersion;
import ru.alefilas.model.moderation.ModerationStatus;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
public class DocumentServiceTest {

    @Autowired
    private DocumentService service;

    @Autowired
    @Qualifier("usersDaoJpa")
    private UsersDao usersDao;

    @Test
    public void saveDocumentTest() {

        DocumentVersion version = new DocumentVersion();
        version.setTitle("title");
        version.setDescription("desc");
        version.setFiles(List.of("C:\\Users\\safil\\Desktop\\javaCourse\\diagram.png",
                "C:\\Users\\safil\\Desktop\\javaCourse\\diagram2.png"));
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

        service.deleteById(savedDocument.getId());

        Assert.assertNull(service.getDocumentById(savedDocument.getId()));
        Assert.assertNull(service.getVersionById(version.getId()));
    }

    @Test
    public void saveDirectoryTest() {

        DirectoryDto root = new DirectoryDto();
        root.setTitle("root");

        DirectoryDto savedRoot = service.save(root);

        DirectoryDto dir = new DirectoryDto();
        dir.setDirectory_id(savedRoot.getId());
        dir.setTitle("dir");

        DirectoryDto savedDir = service.save(dir);

        DirectoryDto rootFromDb = service.getDirectoryById(savedRoot.getId());
        DirectoryDto dirFromDb = service.getDirectoryById(savedDir.getId());

        Assert.assertEquals(savedRoot, rootFromDb);
        Assert.assertEquals(savedDir, dirFromDb);

        service.deleteById(savedRoot.getId());

        Assert.assertNull(service.getDirectoryById(savedRoot.getId()));
        Assert.assertNull(service.getDirectoryById(savedDir.getId()));
    }

    @Test
    public void documentTypesTest() {
        List<DocumentType> listFromDao = service.getAllDocumentTypes();
        Assert.assertTrue(listFromDao.size() > 0);
    }
}
