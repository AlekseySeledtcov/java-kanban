package manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {

    @Test
    public void getDefault() {
        assertNotNull(Managers.getDefault());
    }

    @Test
    public void getDefaultHistory() {
        assertNotNull(Managers.getDefaultHistory());
    }
}