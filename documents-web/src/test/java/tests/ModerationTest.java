package tests;

import app.TestDocumentsApp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.alefilas.dto.documents.InputDocumentDto;
import ru.alefilas.dto.documents.OutputDocumentDto;
import ru.alefilas.dto.moderation.ModerationResult;
import ru.alefilas.dto.moderation.ModerationTicketDto;
import ru.alefilas.model.moderation.ModerationStatus;

@SpringBootTest(classes = TestDocumentsApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class ModerationTest {

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

    private final String host = "http://localhost:";

    @Test
    public void moderationTest() throws JsonProcessingException {

        InputDocumentDto document = mapper.readValue(documentsJson, InputDocumentDto.class);

        OutputDocumentDto dto = restTemplate.withBasicAuth("Alex", "123")
                .postForObject(host + port + "/documents", document, OutputDocumentDto.class);
        Assertions.assertEquals(ModerationStatus.ON_MODERATION, dto.getStatus());

        ModerationTicketDto ticketDto = restTemplate.withBasicAuth("Alex", "123")
                .getForObject(host + port + "/moderation/{id}", ModerationTicketDto.class, dto.getId());

        ModerationResult result = new ModerationResult();
        result.setTicketId(ticketDto.getId());
        result.setResult(ModerationStatus.CONFIRMED);

        int code = restTemplate.withBasicAuth("Alex", "123")
                .postForEntity(host + port + "/moderation", result, String.class).getStatusCodeValue();
        Assertions.assertEquals(200, code);

        dto = restTemplate.withBasicAuth("Alex", "123")
                .getForObject(host + port + "/documents/{id}",  OutputDocumentDto.class, dto.getId());
        Assertions.assertEquals(ModerationStatus.CONFIRMED, dto.getStatus());

        restTemplate.withBasicAuth("Alex", "123")
                .delete(host + port + "/documents/{od}", dto.getId());
    }
}
