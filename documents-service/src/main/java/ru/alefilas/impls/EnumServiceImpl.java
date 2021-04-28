package ru.alefilas.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alefilas.EnumsDao;
import ru.alefilas.EnumService;

import java.util.List;

@Service
public class EnumServiceImpl implements EnumService {

    private final EnumsDao dao;

    @Autowired
    public EnumServiceImpl(EnumsDao dao) {
        this.dao = dao;
    }

    @Override
    public List<String> findAllDocumentTypes() {
        return dao.findAllDocumentTypes();
    }
}
