package ru.alefilas.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class ServletUtils {

    private static ObjectMapper mapper;

    @Autowired
    public ServletUtils(ObjectMapper mapper) {
        ServletUtils.mapper = mapper;
    }

    @PostConstruct
    public void init() {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public static void writeJson(HttpServletResponse resp, Object obj) {
        resp.setContentType("application/json");
        try {
            resp.getOutputStream().write(mapper.writeValueAsBytes(obj));
            resp.getOutputStream().close();
        } catch (IOException e) {
            throw new RuntimeException(Integer.toString(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
        }
    }

    public static <T> T readJson(HttpServletRequest req, Class<T> clazz) throws IOException {
        String text = new BufferedReader(
                new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8))
        .lines()
                .collect(Collectors.joining("\n"));
        req.getInputStream().close();
        return mapper.readValue(text, clazz);
    }
}
