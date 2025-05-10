package memory;

import file.NotFoundException;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest extends TaskManagerTest {

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
        super.beforeEach();
    }

    @Test
    public void checkThatTasksFromHistoryAreReturnedInTheCorrectOrder() throws NotFoundException {
        manager.getTaskById(task2.getId());
        manager.getSubtaskById(subtask1.getId());
        manager.getEpicById(epic.getId());
        manager.getTaskById(task1.getId());
        Task[] expectedArray = new Task[]{task2, subtask1, epic, task1};
        Task[] actualArray = manager.getHistory().toArray(new Task[4]);
        assertArrayEquals(expectedArray, actualArray, "");
    }

    @Test
    public void deleteTasksAndCheckThatTheyHaveBeenRemovedFromHistory() throws NotFoundException {
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        manager.getEpicById(epic.getId());
        manager.getSubtaskById(subtask1.getId());
        manager.removeTaskById(task2.getId());
        Task[] expectedArray = new Task[]{task1, epic, subtask1};
        Task[] actualArray = manager.getHistory().toArray(new Task[3]);
        assertArrayEquals(expectedArray, actualArray, "Тест удаления задания из середины истории - " +
                "ПРОВАЛЕН!");
    }

    @Test
    public void displayEmptyTaskHistory() {
        manager.getHistory();
        assertTrue(manager.getHistory().isEmpty(), "История задач не пуста");
    }

    @Test
    public void duplication() throws NotFoundException {
        manager.getSubtaskById(subtask1.getId());
        manager.getSubtaskById(subtask1.getId());
        manager.getSubtaskById(subtask1.getId());
        assertEquals(List.of(subtask1), manager.getHistory());
    }

    @Test
    public void deleteFromHistoryFromBeginning() throws NotFoundException {
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        manager.getEpicById(epic.getId());
        manager.getSubtaskById(subtask1.getId());
        manager.getSubtaskById(subtask2.getId());
        manager.getSubtaskById(subtask3.getId());
        manager.removeTaskById(task1.getId());
        assertEquals(manager.getHistory(), List.of(task2, epic, subtask1, subtask2, subtask3), "Тест удаления " +
                "задания из середины истории - ПРОВАЛЕН!");
    }

    @Test
    public void deleteFromHistoryFromTheEnd() throws NotFoundException {
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        manager.getEpicById(epic.getId());
        manager.getSubtaskById(subtask1.getId());
        manager.getSubtaskById(subtask2.getId());
        manager.getSubtaskById(subtask3.getId());
        manager.removeSubtaskById(subtask3.getId());
        assertEquals(manager.getHistory(), List.of(task1, task2, epic, subtask1, subtask2), "Тест удаления " +
                "задания из конца истории - ПРОВАЛЕН!");
    }

}