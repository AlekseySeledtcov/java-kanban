package model;

import memory.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubtaskTest {
    private final InMemoryTaskManager taskManager = new InMemoryTaskManager();
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    public void beforeEach() {
        subtask1 = new Subtask("Name Test model.Subtask", "Description Test model.Subtask", 0, 20);
        subtask2 = new Subtask("Name Test model.Subtask", "Description Test model.Subtask", 0, 20);

    }

    @Test
    public void checkThatInstancesOfTheTaskClassAreEqualToEachOther() {
        assertEquals(subtask1.hashCode(), subtask2.hashCode(), "Объекты не равны друг другу");
    }

    @Test
    public void subtaskObjectCannotBeMadeItsOwnEpic() {
        taskManager.addSubtask(subtask1);
        assertEquals(0, taskManager.getSubtasks().size(), "Субтаск не может сам себя назначить эпиком");
    }
}