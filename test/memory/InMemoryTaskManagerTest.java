package memory;

import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() throws IOException {
        manager = new InMemoryTaskManager();
        super.beforeEach();
    }
}