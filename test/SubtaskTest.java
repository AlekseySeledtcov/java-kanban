import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubtaskTest {
    private final InMemoryTaskManager taskManager = new InMemoryTaskManager();
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    public void beforeEach() {
        subtask1 = new Subtask("Name Test Subtask", "Description Test Subtask", 0);
        subtask2 = new Subtask("Name Test Subtask", "Description Test Subtask", 0);

    }
    @Test
    void checkThatInstancesOfTheTaskClassAreEqualToEachOther() {
        assertEquals(subtask1.hashCode(), subtask2.hashCode(), "Объекты не равны друг другу");
    }

    @Test
    void SubtaskObjectCannotBeMadeItsOwnEpic() {
        assertNull(taskManager.addSubtask(subtask1), "Объект Subtask не может быть эпиком для самого себя");
    }
}