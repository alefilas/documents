import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.eclipse.jetty.server.Server;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.alefilas.config.WebConfig;
import ru.alefilas.dto.DocumentDto;
import ru.alefilas.model.document.DocumentPriority;
import ru.alefilas.model.document.DocumentVersion;
import ru.alefilas.model.moderation.ModerationStatus;
import ru.alefilas.model.user.Role;
import ru.alefilas.model.user.User;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class})
public class DocumentServletTest {

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private ObjectMapper mapper;

    private Server server;

    @Before
    public void startServer() throws Exception {
        server = StartJetty.init("src/main/webapp");
        server.start();
    }

    @After
    public void stopServer() throws Exception {
        server.stop();
    }

    @Test
    public void documentTest() throws IOException, URISyntaxException {
        DocumentDto doc = save();
        DocumentDto docFromDb = load(doc.getId());

        Assert.assertEquals(doc, docFromDb);
        Assert.assertEquals(doc.getCurrentVersion(), docFromDb.getCurrentVersion());

        delete(doc.getId());
    }

    private DocumentDto save() throws IOException {
        HttpPost post = new HttpPost(server.getURI() + "document");

        DocumentVersion version = new DocumentVersion();
        version.setTitle("title");
        version.setDescription("desc");
        version.setFiles(List.of("C:\\Users\\safil\\Desktop\\javaCourse\\diagram.png",
                "C:\\Users\\safil\\Desktop\\javaCourse\\diagram2.png"));
        version.setStatus(ModerationStatus.ON_MODERATION);

        User user = new User();
        user.setId(1L);
        user.setName("Alex");
        user.setRole(Role.ADMIN);
        user.setEmail("123@mail.ru");

        DocumentDto document = new DocumentDto();
        document.setDocumentPriority(DocumentPriority.HIGH.toString());
        document.setType("FAX");
        document.setUser_id(1L);
        document.setCurrentVersion(version);
        document.setStatus(ModerationStatus.ON_MODERATION.toString());

        StringEntity entityRoot = new StringEntity(mapper.writeValueAsString(document), ContentType.APPLICATION_JSON);
        post.setEntity(entityRoot);
        CloseableHttpResponse resp = httpClient.execute(post);
        DocumentDto savedDocument = mapper.readValue(resp.getEntity().getContent(), DocumentDto.class);

        Assert.assertEquals(200, resp.getStatusLine().getStatusCode());

        return savedDocument;
    }

    private DocumentDto load(Long id) throws IOException, URISyntaxException {
        HttpGet get = new HttpGet(server.getURI() + "document");
        URI uri = new URIBuilder(get.getURI())
                .addParameter("id", id.toString())
                .build();
        get.setURI(uri);


        CloseableHttpResponse respRoot = httpClient.execute(get);
        return mapper.readValue(respRoot.getEntity().getContent(), DocumentDto.class);
    }

    private void delete(Long id) throws IOException, URISyntaxException {

        HttpDelete delete = new HttpDelete(server.getURI() + "document");
        URI uri = new URIBuilder(delete.getURI())
                .addParameter("id", id.toString())
                .build();
        delete.setURI(uri);

        CloseableHttpResponse resp = httpClient.execute(delete);

        Assert.assertEquals(200, resp.getStatusLine().getStatusCode());
    }

}
