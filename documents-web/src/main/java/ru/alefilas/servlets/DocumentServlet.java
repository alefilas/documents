package ru.alefilas.servlets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alefilas.DocumentService;
import ru.alefilas.dto.DocumentDto;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class DocumentServlet extends HttpServlet {

    private static DocumentService service;

    @Autowired
    public void setService(DocumentService service) {
        DocumentServlet.service = service;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

        if (id != null) {
            ServletUtils.writeJson(resp, service.getDocumentById(Long.parseLong(id)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DocumentDto document = ServletUtils.readJson(req, DocumentDto.class);
        DocumentDto savedDocument = service.save(document);
        ServletUtils.writeJson(resp, savedDocument);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

        if (id != null) {
            service.deleteById("entity", Long.parseLong(id));
        }
    }
}
