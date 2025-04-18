import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    T manager;
    Task task1;
    Task task2;
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    List<Task> list = new ArrayList<>();

    @BeforeEach
    void beforeEach() {
        task1 = new Task("Name Test Task1", "Description Test Task1", 20);
        task2 = new Task("Name Test Task2", "Description Test Task2", 20);
        epic = new Epic("Name Test Epic", "Description Test Epic");
        subtask1 = new Subtask("Name Test Subtask1", "Description Test Subtask1", 2, 10);
        subtask2 = new Subtask("Name Test Subtask2", "Description Test Subtask2", 2, 15);
        subtask3 = new Subtask("Name Test Subtask3", "Description Test Subtask3", 2, 20);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
    }

    @Test
    void giveTasksOfDifferentTypesAndFindThemById() {
        int idTask = 0;
        int idSubtask = 0;
        int idEpic = 0;
        list.addAll(manager.getListFromHashTask());
        list.addAll(manager.getListFromHashEpic());
        list.addAll(manager.getListFromHashSubtask());
        for (Task item : list) {
            if (item.equals(task1)) {
                idTask = item.getId();
            } else if (item.equals(epic)) {
                idEpic = item.getId();
            } else if (item.equals(subtask1)) {
                idSubtask = item.getId();
            }
        }
        assertEquals(task1, manager.getTaskById(idTask), "Задачи Task не совпадают");
        assertEquals(epic, manager.getEpicById(idEpic), "Задачи Epic не совпадают");
        assertEquals(subtask1, manager.getSubtaskById(idSubtask), "Задачи Subtask не совпадают");
    }

    @Test
    void checkThatTasksWithTheGivenIdAndTheGeneratedDoNotConflict() {
        task1.setId(0);
        int idTask1 = 0;
        int idTask2 = 0;
        manager.addTask(task1);
        list = manager.getListFromHashTask();
        for (Task task : list) {
            if (task.equals(task1)) {
                idTask1 = task.getId();
            }
        }
        task2.setId(idTask1);
        manager.addTask(task2);
        list = manager.getListFromHashTask();
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
        list.addAll(manager.getListFromHashTask());
        list.addAll(manager.getListFromHashEpic());
        list.addAll(manager.getListFromHashSubtask());
        for (Task task : list) {
            if (task.equals(task1)) {
                actualTask = task;
            } else if (task.equals(epic)) {
                actualEpic = task;
            } else if (task.equals(subtask1)) {
                actualSubtask = task;
            }
        }
        assertEquals(task1, actualTask, "Поля Task не равны");
        assertEquals(epic, actualEpic, "Поля Epic не равны");
        assertEquals(subtask1, actualSubtask, "Поля Subtask не равны");
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
        list.addAll(manager.getListFromHashTask());
        list.addAll(manager.getListFromHashEpic());
        list.addAll(manager.getListFromHashSubtask());
        for (Task item : list) {
            if (item.equals(task1)) {
                idTask = item.getId();
            } else if (item.equals(epic)) {
                idEpic = item.getId();
            } else if (item.equals(subtask1)) {
                idSubtask = item.getId();
            }
        }
        manager.getTaskById(idTask);
        manager.getEpicById(idEpic);
        manager.getSubtaskById(idSubtask);
        historyList = manager.getHistory();
        for (Task task : historyList) {
            if (task.equals(task1)) {
                actualTask = task;
            } else if (task.equals(epic)) {
                actualEpic = task;
            } else if (task.equals(subtask1)) {
                actualSubtask = task;
            }
        }
        assertEquals(task1, actualTask, "Значение task1 не сопадает со значением полученным из истории");
        assertEquals(epic, actualEpic, "Значение epic не сопадает со значением полученным из истории");
        assertEquals(subtask1, actualSubtask, "Значение subtask не сопадает со значением полученным из истории");
    }

    @Test
    void calculationOfBoundaryValuesAllSubtasksWithStatus() {
        assertEquals(manager.getEpicById(epic.getId()).getStatus(), Status.NEW
                , "Статус задачи Epic не соответствует статусу NEW");
        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);
        assertEquals(manager.getEpicById(epic.getId()).getStatus(), Status.IN_PROGRESS
                , "Статус задачи Epic не соответствует статусу IN_PROGRESS");
        subtask2.setStatus(Status.DONE);
        manager.updateSubtask(subtask2);
        subtask3.setStatus(Status.DONE);
        manager.updateSubtask(subtask3);
        assertEquals(manager.getEpicById(epic.getId()).getStatus(), Status.DONE
                , "Статус задачи Epic не соответствует статусу DONE");
        subtask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask1);
        subtask2.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask2);
        subtask3.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask3);
        assertEquals(manager.getEpicById(epic.getId()).getStatus(), Status.IN_PROGRESS
                , "Статус задачи Epic не соответствует статусу IN_PROGRESS");
    }

    @Test
    void checkingForIntersectionOfTimeIntervals() {
        task2.setStartTime(task2.getStartTime().plusMinutes(10));
        manager.updateTask(task2);
        assertTrue(manager.intersection(task2), "Пересечение временных интервалов не обнаружено");
        subtask1.setStartTime(subtask1.getStartTime().plusMinutes(11));
        manager.updateSubtask(subtask1);
        assertTrue(manager.intersection(subtask1), "Пересечение временных интервалов не обнаружено");
        assertTrue(manager.intersection(epic), "Пересечение временных интервалов не обнаружено");
        task2.setStartTime(task2.getStartTime().plusMinutes(30));
        manager.updateTask(task2);
        assertFalse(manager.intersection(task2), "Пересечение временных интервалов обнаружено");
    }
}
