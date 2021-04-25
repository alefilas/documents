import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.alefilas.model.document.Directory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class DirectoryServletTest {

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
    public void directoryTest() throws IOException, URISyntaxException {
        List<Directory> dirs = save();

        for (Directory dir: dirs) {
            Directory dirFromDb = load(dir.getId());
            Assert.assertEquals(dir, dirFromDb);
        }

        for (Directory dir : dirs) {
            delete(dir.getId());
        }
    }

    private List<Directory> save() throws IOException {
        HttpPost post = new HttpPost(server.getURI() + "directory");

        Directory root = new Directory();
        root.setTitle("root");

        Directory dir = new Directory();
        dir.setParentDirectory(root);
        dir.setTitle("dir");

        StringEntity entityRoot = new StringEntity(mapper.writeValueAsString(root), ContentType.APPLICATION_JSON);
        post.setEntity(entityRoot);
        CloseableHttpResponse respRoot = httpClient.execute(post);
        Directory savedRoot = mapper.readValue(respRoot.getEntity().getContent(), Directory.class);

        dir.setParentDirectory(savedRoot);

        StringEntity entityDir = new StringEntity(mapper.writeValueAsString(dir), ContentType.APPLICATION_JSON);
        post.setEntity(entityDir);
        CloseableHttpResponse respDir = httpClient.execute(post);
        Directory savedDir = mapper.readValue(respDir.getEntity().getContent(), Directory.class);

        Assert.assertEquals(200, respRoot.getStatusLine().getStatusCode());
        Assert.assertEquals(200, respDir.getStatusLine().getStatusCode());

        return List.of(savedRoot, savedDir);
    }

    private Directory load(Long id) throws IOException, URISyntaxException {
        HttpGet get = new HttpGet(server.getURI() + "directory");
        URI uri = new URIBuilder(get.getURI())
                .addParameter("id", id.toString())
                .build();
        get.setURI(uri);


        CloseableHttpResponse respRoot = httpClient.execute(get);
        return mapper.readValue(respRoot.getEntity().getContent(), Directory.class);
    }

    private void delete(Long id) throws IOException, URISyntaxException {

        HttpDelete delete = new HttpDelete(server.getURI() + "directory");
        URI uri = new URIBuilder(delete.getURI())
                .addParameter("id", id.toString())
                .build();
        delete.setURI(uri);

        CloseableHttpResponse resp = httpClient.execute(delete);

        Assert.assertEquals(200, resp.getStatusLine().getStatusCode());
    }

}
