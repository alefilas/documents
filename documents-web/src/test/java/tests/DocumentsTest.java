package tests;

import app.TestDocumentsApp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.objectweb.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.alefilas.dto.documents.InputDocumentDto;
import ru.alefilas.dto.documents.InputDocumentVersionDto;
import ru.alefilas.dto.documents.OutputDocumentDto;
import ru.alefilas.dto.documents.OutputDocumentVersionDto;
import ru.alefilas.model.document.DocumentPriority;

import java.util.List;

@SpringBootTest(classes = TestDocumentsApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class DocumentsTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    private final String documentsJson = "{\n" +
            "  \"currentVersion\": {\n" +
            "    \"description\": \"string\",\n" +
            "    \"files\": [\n" +
            "      \"string\"\n" +
            "    ],\n" +
            "    \"title\": \"string\"\n" +
            "  },\n" +
            "  \"directoryId\": 0,\n" +
            "  \"documentPriority\": \"HIGH\",\n" +
            "  \"id\": 0,\n" +
            "  \"type\": \"letter\"\n" +
            "}";

    private final String versionJson = "{\n" +
            "  \"description\": \"string\",\n" +
            "  \"files\": [\n" +
            "    \"string\"\n" +
            "  ],\n" +
            "  \"title\": \"new title\"\n" +
            "}";

    private final String host = "http://localhost:";

    @Test
    public void documentUpdateTest() throws JsonProcessingException {

        InputDocumentDto inputDocumentDto = mapper.readValue(documentsJson, InputDocumentDto.class);

        OutputDocumentDto documentDto1 = restTemplate.withBasicAuth("Alex", "123")
                .postForObject(host + port + "/documents", inputDocumentDto, OutputDocumentDto.class);

        inputDocumentDto.setId(documentDto1.getId());
        inputDocumentDto.getCurrentVersion().setTitle("new title");

        OutputDocumentDto documentDto2 = restTemplate.withBasicAuth("Alex", "123")
                .postForObject(host + port + "/documents", inputDocumentDto, OutputDocumentDto.class);

        Assertions.assertEquals(inputDocumentDto.getCurrentVersion().getTitle(), documentDto2.getCurrentVersion().getTitle());
        Assertions.assertNotEquals(documentDto1.getCurrentVersion().getId(), documentDto2.getCurrentVersion().getId());

        inputDocumentDto.getCurrentVersion().setTitle("string");
        inputDocumentDto.setDocumentPriority(DocumentPriority.LOW);

        OutputDocumentDto documentDto3 = restTemplate.withBasicAuth("Alex", "123")
                .postForObject(host + port + "/documents", inputDocumentDto, OutputDocumentDto.class);

        Assertions.assertEquals(inputDocumentDto.getDocumentPriority(), documentDto3.getDocumentPriority());
        Assertions.assertEquals(documentDto1.getCurrentVersion().getId(), documentDto3.getCurrentVersion().getId());
        Assertions.assertEquals(DocumentPriority.LOW, documentDto3.getDocumentPriority());

        restTemplate.withBasicAuth("Alex", "123")
                .delete(host + port + "/documents/{id}", documentDto1.getId());
    }

    @Test
    public void documentVersionsTest() throws JsonProcessingException {
        InputDocumentDto inputDocumentDto = mapper.readValue(documentsJson, InputDocumentDto.class);

        OutputDocumentDto documentDto1 = restTemplate.withBasicAuth("Alex", "123")
                .postForObject(host + port + "/documents", inputDocumentDto, OutputDocumentDto.class);

        InputDocumentVersionDto dto = mapper.readValue(versionJson, InputDocumentVersionDto.class);

        OutputDocumentVersionDto documentVersionDto = restTemplate.withBasicAuth("Alex", "123")
                .postForObject(host + port + "/documents/{id}/versions",
                        dto,
                        OutputDocumentVersionDto.class,
                        documentDto1.getId());

        Assertions.assertNotEquals(documentDto1.getCurrentVersion().getTitle(), documentVersionDto.getTitle());
        Assertions.assertNotEquals(documentDto1.getCurrentVersion().getId(), documentVersionDto.getId());

        inputDocumentDto.getCurrentVersion().setTitle("string");

        documentVersionDto = restTemplate.withBasicAuth("Alex", "123")
                .postForObject(host + port + "/documents/{id}/versions",
                        inputDocumentDto.getCurrentVersion(),
                        OutputDocumentVersionDto.class,
                        documentDto1.getId());

        Assertions.assertEquals(documentDto1.getCurrentVersion().getId(), documentVersionDto.getId());

        ResponseEntity<List<OutputDocumentVersionDto>> dtoResponse = restTemplate.withBasicAuth("Alex", "123")
                .exchange(
                        host + port + "/documents/{id}/versions", HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<OutputDocumentVersionDto>>() {},
                        documentDto1.getId()
                );

        List<OutputDocumentVersionDto> exampleList = dtoResponse.getBody();

        Assertions.assertNotNull(exampleList);
        Assertions.assertEquals(2, exampleList.size());

        restTemplate.withBasicAuth("Alex", "123")
                .delete(host + port + "/documents/{id}", documentDto1.getId());
    }
}
