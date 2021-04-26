import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.checkerframework.checker.units.qual.A;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.alefilas.dto.DocumentDto;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.document.DocumentPriority;
import ru.alefilas.model.document.DocumentVersion;
import ru.alefilas.model.moderation.ModerationStatus;
import ru.alefilas.model.user.Role;
import ru.alefilas.model.user.User;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DocumentServletTest {

    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final ObjectMapper mapper = new ObjectMapper();
    private Server server;

    {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

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
        version.setFiles(List.of(Path.of("C:\\Users\\safil\\Desktop\\javaCourse\\diagram.png"),
                Path.of("C:\\Users\\safil\\Desktop\\javaCourse\\diagram2.png")));
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
