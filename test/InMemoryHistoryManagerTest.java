import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private final InMemoryTaskManager taskManager = new InMemoryTaskManager();
    private Task task1;
    private Task task2;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void beforeEach() {
        task1 = new Task("Name Test Task1", "Description Test Task1");
        task2 = new Task("Name Test Task2", "Description Test Task2");
        epic = new Epic("Name Test Epic", "Description Test Epic");
        subtask = new Subtask("Name Test Subtask", "Description Test Subtask", 2);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
    }

    @Test
    void CheckThatTasksFromHistoryAreReturnedInTheCorrectOrder() {
        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getSubtaskById(subtask.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getTaskById(task1.getId());


        Task[] expectedArray = new Task[]{task2, subtask, epic, task1};
        Task[] actualArray = taskManager.getHistory().toArray(new Task[4]);
        assertArrayEquals(expectedArray, actualArray, "");
    }

    @Test
    void DeleteTasksAndCheckThatTheyHaveBeenRemovedFromHistory() {
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask.getId());
        taskManager.removeEpicById(epic.getId());

        Task[] expectedArray = new Task[]{task1, task2};
        Task[] actualArray = taskManager.getHistory().toArray(new Task[2]);
        assertArrayEquals(expectedArray, actualArray);
    }

}