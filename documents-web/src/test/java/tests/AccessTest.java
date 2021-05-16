package tests;

import app.TestDocumentsApp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.alefilas.dto.documents.InputDirectoryDto;
import ru.alefilas.dto.documents.OutputDirectoryDto;
import ru.alefilas.dto.permit.PermitDto;
import ru.alefilas.model.permit.PermitType;

import java.util.List;
import java.util.Objects;

@SpringBootTest(classes = TestDocumentsApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class AccessTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final String host = "http://localhost:";

    @Test
    public void testAuthorize() {
        int code = restTemplate.withBasicAuth("Qwer", "qwer").
                getForEntity(host + port + "/admin/email/account", String.class).getStatusCodeValue();
        Assertions.assertEquals(401, code);

        code = restTemplate.withBasicAuth("Petr", "petr").
                getForEntity(host + port + "/admin/email/account", String.class).getStatusCodeValue();
        Assertions.assertEquals(403, code);

        code = restTemplate.withBasicAuth("Petr", "petr").
                getForEntity(host + port + "/moderation/all?page=0", String.class).getStatusCodeValue();
        Assertions.assertEquals(200, code);

        code = restTemplate.withBasicAuth("Alex", "123").
                getForEntity(host + port + "/admin/email/account", String.class).getStatusCodeValue();
        Assertions.assertEquals(200, code);
    }

    @Test
    public void testWritePermit() {
        List<Long> directories = new java.util.ArrayList<>(createDirectories());

        PermitDto permitDto = new PermitDto();
        permitDto.setPermitType(PermitType.WRITE);
        permitDto.setDirectoryId(directories.get(1));
        permitDto.setUsername("Ivan");

        PermitDto dto = restTemplate.withBasicAuth("Alex", "123").
                postForObject(host + port + "/admin/permits", permitDto,  PermitDto.class);

        Assertions.assertEquals(dto.getUsername(), permitDto.getUsername());

        InputDirectoryDto directory = new InputDirectoryDto();
        directory.setTitle("title");
        directory.setDirectoryId(directories.get(1));

        ResponseEntity<OutputDirectoryDto> directoryDto = restTemplate.withBasicAuth("Ivan", "ivan").
                postForEntity(host + port + "/directories", directory,  OutputDirectoryDto.class);

        Assertions.assertEquals(200, directoryDto.getStatusCodeValue());

        directories.add(Objects.requireNonNull(directoryDto.getBody()).getId());

        directory.setDirectoryId(directories.get(0));

        ResponseEntity<String> stringResponseEntity = restTemplate.withBasicAuth("Ivan", "ivan").
                postForEntity(host + port + "/directories", directory,  String.class);

        Assertions.assertEquals(403, stringResponseEntity.getStatusCodeValue());

        stringResponseEntity = restTemplate.withBasicAuth("Petr", "petr").
                postForEntity(host + port + "/directories", directory,  String.class);

        Assertions.assertEquals(403, stringResponseEntity.getStatusCodeValue());

        directoryDto = restTemplate.withBasicAuth("Alex", "123").
                postForEntity(host + port + "/directories", directory,  OutputDirectoryDto.class);

        Assertions.assertEquals(200, directoryDto.getStatusCodeValue());

        directories.add(Objects.requireNonNull(directoryDto.getBody()).getId());

        deleteDirectories(directories);
    }

    @Test
    public void readTest() {
        List<Long> directories = createDirectories();

        OutputDirectoryDto directoryDto = restTemplate.withBasicAuth("Alex", "123").
                getForObject(host + port + "/directories/{id}", OutputDirectoryDto.class, directories.get(1));

        Assertions.assertEquals(directories.get(1), directoryDto.getId());

        directoryDto = restTemplate.withBasicAuth("Ivan", "ivan").
                getForObject(host + port + "/directories/{id}", OutputDirectoryDto.class, directories.get(0));

        Assertions.assertEquals(directories.get(0), directoryDto.getId());

        deleteDirectories(directories);
    }

    private List<Long> createDirectories() {

        InputDirectoryDto directory = new InputDirectoryDto();
        directory.setTitle("title");

        OutputDirectoryDto savedDirectory = restTemplate.withBasicAuth("Petr", "petr").
                postForObject(host + port + "/directories", directory,  OutputDirectoryDto.class);

        directory = new InputDirectoryDto();
        directory.setDirectoryId(savedDirectory.getId());
        directory.setTitle("title2");

        savedDirectory = restTemplate.withBasicAuth("Alex", "123").
                postForObject(host + port + "/directories", directory,  OutputDirectoryDto.class);

        return List.of(savedDirectory.getDirectoryId(), savedDirectory.getId());
    }

    private void deleteDirectories(List<Long> directories) {
        for (Long id : directories) {
            restTemplate.withBasicAuth("Alex", "123")
                    .delete(host + port + "/directories/{id}", id);

            int code = restTemplate.withBasicAuth("Alex", "123").
                    getForEntity(host + port + "/directories/{id}", String.class, id).getStatusCodeValue();

            Assertions.assertEquals(404, code);
        }
    }
}
