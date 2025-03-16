import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private final InMemoryTaskManager taskManager = new InMemoryTaskManager();
    private Task task1;
    private Task task2;
    private Epic epic;
    private Subtask subtask;
    private List<Task> list;

    @BeforeEach
    void beforeEach() {
        task1 = new Task("Name Test Task", "Description Test Task");
        task2 = new Task("Name Test Task", "Description Test Task");
        epic = new Epic("Name Test Epic", "Description Test Epic");
        subtask = new Subtask("Name Test Subtask", "Description Test Subtask", 1);
        list = new ArrayList<>();
    }

    @Test
    void giveTasksOfDifferentTypesAndFindThemById() {
        int idTask = 0;
        int idEpic = 0;
        int idSubtask = 0;
        taskManager.addTask(task1);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        list.addAll(taskManager.getListFromHashTask());
        list.addAll(taskManager.getListFromHashEpic());
        list.addAll(taskManager.getListFromHashSubtask());
        for (Task item : list) {
            if (item.equals(task1)) {
                idTask = item.getId();
            } else if (item.equals(epic)) {
                idEpic = item.getId();
            } else if (item.equals(subtask)) {
                idSubtask = item.getId();
            }
        }
        assertEquals(task1, taskManager.getTaskById(idTask), "Задачи Task не совпадают");
        assertEquals(epic, taskManager.getEpicById(idEpic), "Задачи Epic не совпадают");
        assertEquals(subtask, taskManager.getSubtaskById(idSubtask), "Задачи Subtask не совпадают");
    }

    @Test
    void checkThatTasksWithTheGivenIdAndTheGeneratedDoNotConflict() {
        task1.setId(0);
        int idTask1 = 0;
        int idTask2 = 0;
        taskManager.addTask(task1);
        list = taskManager.getListFromHashTask();
        for (Task task : list) {
            if (task.equals(task1)) {
                idTask1 = task.getId();
            }
        }
        task2.setId(idTask1);
        taskManager.addTask(task2);
        list = taskManager.getListFromHashTask();
        for (Task task : list) {
            if (task.equals(task2)) {
                idTask2 = task.getId();
            }
        }
        assertNotEquals(idTask1, idTask2, "Менеджер не присволи новое значение ID");
    }

    @Test
    void CheckingThatTheTaskHasNotChangedInAllFieldsAfterAddingToTheManager() {
        Task actualTask = null;
        Task actualEpic = null;
        Task actualSubtask = null;
        taskManager.addTask(task1);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        list.addAll(taskManager.getListFromHashTask());
        list.addAll(taskManager.getListFromHashEpic());
        list.addAll(taskManager.getListFromHashSubtask());
        for (Task task : list) {
            if (task.equals(task1)) {
                actualTask = task;
            } else if (task.equals(epic)) {
                actualEpic = task;
            } else if (task.equals(subtask)) {
                actualSubtask = task;
            }
        }
        assertEquals(task1, actualTask, "Поля Task не равны");
        assertEquals(epic, actualEpic, "Поля Epic не равны");
        assertEquals(subtask, actualSubtask, "Поля Subtask не равны");
    }

    @Test
    void tasksAddedToHistoryManagerRetainThePreviousVersionOfTheTask() {
        int idTask = 0;
        int idEpic = 0;
        int idSubtask = 0;
        Task actualTask = null;
        Task actualEpic = null;
        Task actualSubtask = null;
        List<Task> historyList;
        taskManager.addTask(task1);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        list.addAll(taskManager.getListFromHashTask());
        list.addAll(taskManager.getListFromHashEpic());
        list.addAll(taskManager.getListFromHashSubtask());
        for (Task item : list) {
            if (item.equals(task1)) {
                idTask = item.getId();
            } else if (item.equals(epic)) {
                idEpic = item.getId();
            } else if (item.equals(subtask)) {
                idSubtask = item.getId();
            }
        }
        taskManager.getTaskById(idTask);
        taskManager.getEpicById(idEpic);
        taskManager.getSubtaskById(idSubtask);
        historyList = taskManager.getHistory();
        for (Task task : historyList) {
            if (task.equals(task1)) {
                actualTask = task;
            } else if (task.equals(epic)) {
                actualEpic = task;
            } else if (task.equals(subtask)) {
                actualSubtask = task;
            }
        }
        assertEquals(task1, actualTask, "Значение task1 не сопадает со значением полученным из истории");
        assertEquals(epic, actualEpic,"Значение epic не сопадает со значением полученным из истории");
        assertEquals(subtask, actualSubtask, "Значение subtask не сопадает со значением полученным из истории");
    }
}