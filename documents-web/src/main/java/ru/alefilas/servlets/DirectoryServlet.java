package ru.alefilas.servlets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.alefilas.DocumentService;
import ru.alefilas.dto.DirectoryDto;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class DirectoryServlet extends HttpServlet {

    private static DocumentService service;

    @Autowired
    public void setService(DocumentService service) {
        DirectoryServlet.service = service;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

        if (id != null) {
            ServletUtils.writeJson(resp, service.getDirectoryById(Long.parseLong(id)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DirectoryDto directory = ServletUtils.readJson(req, DirectoryDto.class);
        DirectoryDto savedDirectory = service.save(directory);
        ServletUtils.writeJson(resp, savedDirectory);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

        if (id != null) {
            service.deleteById("entity", Long.parseLong(id));
        }
    }
}
