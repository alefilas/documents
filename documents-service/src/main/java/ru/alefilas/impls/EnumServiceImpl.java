package ru.alefilas.impls;

import ru.alefilas.EnumsDao;
import ru.alefilas.EnumService;

import java.util.List;

public class EnumServiceImpl implements EnumService {

    private EnumsDao dao;

    public EnumServiceImpl(EnumsDao dao) {
        this.dao = dao;
    }

    @Override
    public List<String> findAllDocumentTypes() {
        return dao.findAllDocumentTypes();
    }
}
