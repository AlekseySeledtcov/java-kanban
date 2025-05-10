package memory;

import file.ManagerSaveException;
import file.NotFoundException;
import file.Status;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    T manager;
    Task task1;
    Task task2;
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    List<Task> list = new ArrayList<>();

    @BeforeEach
    public void beforeEach() {
        task1 = new Task("Name Test Task1", "Description Test Task1", 10);
        task2 = new Task("Name Test Task2", "Description Test Task2", 10);
        epic = new Epic("Name Test model.Epic", "Description Test model.Epic");
        subtask1 = new Subtask("Name Test Subtask1", "Description Test Subtask1", 2, 10);
        subtask2 = new Subtask("Name Test Subtask2", "Description Test Subtask2", 2, 10);
        subtask3 = new Subtask("Name Test Subtask3", "Description Test Subtask3", 2, 10);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        task1.setStartTime(LocalDateTime.parse("24.04.2025, 10:00", dtf));
        task1.setEndTime(LocalDateTime.parse("24.04.2025, 10:10", dtf));
        task2.setStartTime(LocalDateTime.parse("24.04.2025, 10:11", dtf));
        task2.setEndTime(LocalDateTime.parse("24.04.2025, 10:21", dtf));
        subtask1.setStartTime(LocalDateTime.parse("24.04.2025, 10:22", dtf));
        subtask1.setEndTime(LocalDateTime.parse("24.04.2025, 10:32", dtf));
        subtask2.setStartTime(LocalDateTime.parse("24.04.2025, 10:33", dtf));
        subtask2.setEndTime(LocalDateTime.parse("24.04.2025, 10:43", dtf));
        subtask3.setStartTime(LocalDateTime.parse("24.04.2025, 10:44", dtf));
        subtask3.setEndTime(LocalDateTime.parse("24.04.2025, 10:54", dtf));
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
    }

    @Test
    public void giveTasksOfDifferentTypesAndFindThemById() throws NotFoundException {
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
        assertEquals(task1, manager.getTaskById(idTask), "Задачи model.Task не совпадают");
        assertEquals(epic, manager.getEpicById(idEpic), "Задачи model.Epic не совпадают");
        assertEquals(subtask1, manager.getSubtaskById(idSubtask), "Задачи model.Subtask не совпадают");
    }

    @Test
    public void checkThatTasksWithTheGivenIdAndTheGeneratedDoNotConflict() {
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
    public void checkingThatTheTaskHasNotChangedInAllFieldsAfterAddingToTheManager() {
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
        assertEquals(task1, actualTask, "Поля model.Task не равны");
        assertEquals(epic, actualEpic, "Поля model.Epic не равны");
        assertEquals(subtask1, actualSubtask, "Поля model.Subtask не равны");
    }

    @Test
    public void tasksAddedToHistoryManagerRetainThePreviousVersionOfTheTask() throws NotFoundException {
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
    public void calculationOfBoundaryValuesAllSubtasksWithStatus() throws NotFoundException {
        assertEquals(manager.getEpicById(epic.getId()).getStatus(), Status.NEW
                , "Статус задачи model.Epic не соответствует статусу NEW");
        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);
        assertEquals(manager.getEpicById(epic.getId()).getStatus(), Status.IN_PROGRESS
                , "Статус задачи model.Epic не соответствует статусу IN_PROGRESS");
        subtask2.setStatus(Status.DONE);
        manager.updateSubtask(subtask2);
        subtask3.setStatus(Status.DONE);
        manager.updateSubtask(subtask3);
        assertEquals(manager.getEpicById(epic.getId()).getStatus(), Status.DONE
                , "Статус задачи model.Epic не соответствует статусу DONE");
        subtask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask1);
        subtask2.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask2);
        subtask3.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask3);
        assertEquals(manager.getEpicById(epic.getId()).getStatus(), Status.IN_PROGRESS
                , "Статус задачи model.Epic не соответствует статусу IN_PROGRESS");
    }

    @Test
    public void checkingForIntersectionOfTimeIntervals() {
        assertFalse(manager.intersection(task2), "Пересечение временных интервалов Task2");
        assertFalse(manager.intersection(subtask1), "Пересечение временных интервалов Subtask1");
        Task task3 = new Task("Name Test Task3", "Description Test Task3", 10);
        Task task4 = new Task("Name Test Task4", "Description Test Task4", 10);

        assertThrows(ManagerSaveException.class, () -> {
            manager.addTask(task3);
            manager.addTask(task4);
        }, "Наложение временных интервалов не обнаружено");
//        assertTrue(manager.intersection(task4), "Пересечение временных интервалов обнаружено");
    }
}
