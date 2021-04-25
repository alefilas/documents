package ru.alefilas.servlets;

import ru.alefilas.DocumentService;
import ru.alefilas.impls.DocumentServiceImpls;
import ru.alefilas.impls.DocumentsDaoJdbc;
import ru.alefilas.model.document.Document;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DocumentServlet extends HttpServlet {

    private final DocumentService service = new DocumentServiceImpls(new DocumentsDaoJdbc());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

        if (id != null) {
            ServletUtils.writeJson(resp, service.findDocumentById(Long.parseLong(id)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Document document = ServletUtils.readJson(req, Document.class);
        Document savedDocument = service.save(document);
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
