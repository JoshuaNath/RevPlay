import service.AuthService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RevPlayTest {

    @Test
    void loginTest() throws Exception {
        AuthService auth = new AuthService();
        assertNotNull(auth);
    }
}