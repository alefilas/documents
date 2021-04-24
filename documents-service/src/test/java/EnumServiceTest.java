import org.junit.Assert;
import org.junit.Test;
import ru.alefilas.EnumService;
import ru.alefilas.impls.EnumServiceImpl;
import ru.alefilas.impls.EnumsDaoJdbc;

import java.util.List;

public class EnumServiceTest {

    private final EnumService service = new EnumServiceImpl(new EnumsDaoJdbc());

    @Test
    public void documentTypesTest() {
        List<String> listFromDao = service.findAllDocumentTypes();
        Assert.assertTrue(listFromDao.size() > 0);
    }
}
