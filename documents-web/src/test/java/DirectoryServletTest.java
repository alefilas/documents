import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.alefilas.config.WebConfig;
import ru.alefilas.dto.DirectoryDto;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class})
public class DirectoryServletTest {

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private ObjectMapper mapper;

    private Server server;

    @Before
    public void startServer() throws Exception {
        server = StartJetty.init("src/main/webapp");
        server.start();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @After
    public void stopServer() throws Exception {
        server.stop();
    }

    @Test
    public void directoryTest() throws IOException, URISyntaxException {

        List<DirectoryDto> dirs = save();

        for (DirectoryDto dir: dirs) {
            DirectoryDto dirFromDb = load(dir.getId());
            Assert.assertEquals(dir, dirFromDb);
        }

        for (DirectoryDto dir : dirs) {
            delete(dir.getId());
        }
    }

    private List<DirectoryDto> save() throws IOException {
        HttpPost post = new HttpPost(server.getURI() + "directory");

        DirectoryDto root = new DirectoryDto();
        root.setTitle("root");

        StringEntity entityRoot = new StringEntity(mapper.writeValueAsString(root), ContentType.APPLICATION_JSON);
        post.setEntity(entityRoot);
        CloseableHttpResponse respRoot = httpClient.execute(post);
        DirectoryDto savedRoot = mapper.readValue(respRoot.getEntity().getContent(), DirectoryDto.class);

        DirectoryDto dir = new DirectoryDto();
        dir.setDirectory_id(savedRoot.getId());
        dir.setTitle("dir");

        StringEntity entityDir = new StringEntity(mapper.writeValueAsString(dir), ContentType.APPLICATION_JSON);
        post.setEntity(entityDir);
        CloseableHttpResponse respDir = httpClient.execute(post);
        DirectoryDto savedDir = mapper.readValue(respDir.getEntity().getContent(), DirectoryDto.class);

        Assert.assertEquals(200, respRoot.getStatusLine().getStatusCode());
        Assert.assertEquals(200, respDir.getStatusLine().getStatusCode());

        return List.of(savedRoot, savedDir);
    }

    private DirectoryDto load(Long id) throws IOException, URISyntaxException {
        HttpGet get = new HttpGet(server.getURI() + "directory");
        URI uri = new URIBuilder(get.getURI())
                .addParameter("id", id.toString())
                .build();
        get.setURI(uri);


        CloseableHttpResponse respRoot = httpClient.execute(get);
        return mapper.readValue(respRoot.getEntity().getContent(), DirectoryDto.class);
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
