package server;

import com.google.gson.reflect.TypeToken;
import file.NotFoundException;
import memory.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.HttpUtils;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    HttpTaskServer server;
    InMemoryTaskManager manager;
    Task task1;
    Task task2;
    Subtask subtask3;
    Subtask subtask4;
    Epic epic5;
    HttpUtils httpUtils;

    public class TaskListTypeToken extends TypeToken<List<Task>> {
    }

    public class SubtaskListTypeToken extends TypeToken<List<Subtask>> {
    }

    public class EpicListTypeToken extends TypeToken<List<Epic>> {
    }

    public class IntegerListTypeToken extends TypeToken<List<Integer>> {
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        httpUtils = new HttpUtils();
        manager = new InMemoryTaskManager();
        server = new HttpTaskServer(manager);
        server.start();

        task1 = new Task("ИмяTask1", "ОписаниеTask1", 9);
        task1.setStartTime(LocalDateTime.parse("2025.05.05 12:00", Task.formatter));
        task1.setEndTime(task1.getStartTime().plusMinutes(9));
        task2 = new Task("ИмяTask2", "ОписаниеTask2", 9);
        task2.setStartTime(LocalDateTime.parse("2025.05.05 12:10", Task.formatter));
        task2.setEndTime(task2.getStartTime().plusMinutes(9));
        subtask3 = new Subtask("ИмяSubtask3", "ОписаниеSubtask3", 0, 9);
        subtask3.setStartTime(LocalDateTime.parse("2025.05.05 13:00", Task.formatter));
        subtask3.setEndTime(subtask3.getStartTime().plusMinutes(9));
        subtask4 = new Subtask("ИмяSubtask4", "ОписаниеSubtask4", 0, 9);
        subtask4.setStartTime(LocalDateTime.parse("2025.05.05 13:10", Task.formatter));
        subtask4.setEndTime(subtask4.getStartTime().plusMinutes(9));
        epic5 = new Epic("ИмяEpic5", "ОписаниеEpic5");
    }

    @Test
    public void checkThatTasksAreAddedToTheManager() throws IOException, InterruptedException {
        String epicJson = httpUtils.getGson().toJson(epic5);
        String taskJson = httpUtils.getGson().toJson(task1);
        String subtaskJson = httpUtils.getGson().toJson(subtask3);

        HttpResponse<String> epicResponse = httpUtils.sendPOSTRequest("http://localhost:8080/epics", epicJson);
        HttpResponse<String> taskResponse = httpUtils.sendPOSTRequest("http://localhost:8080/tasks", taskJson);
        HttpResponse<String> SubtaskResponse = httpUtils.sendPOSTRequest("http://localhost:8080/subtasks",
                subtaskJson);

        assertEquals(201, taskResponse.statusCode(), "Код ответа задачи Task не совпадает");
        assertEquals(201, epicResponse.statusCode(), "Код ответа задачи Epic не совпадает");
        assertEquals(201, SubtaskResponse.statusCode(), "Код ответа задачи Subtask не совпадает");
        assertNotNull(manager.getListFromHashTask(), "Задачи Task не возвращаются");
        assertNotNull(manager.getListFromHashEpic(), "Задачи Epic не возвращаются");
        assertNotNull(manager.getListFromHashSubtask(), "Задачи Subtask не возвращаются");
        assertEquals(1, manager.getListFromHashTask().size(), "Некоректное колличество задач Task");
        assertEquals(1, manager.getListFromHashEpic().size(), "Некоректное колличество задач Epic");
        assertEquals(1, manager.getListFromHashSubtask().size(),
                "Некоректное колличество задач Subtask");
        assertEquals(task1.getName(), manager.getListFromHashTask().get(0).getName(), "Поля у задачи " +
                "Task из менеджера, и полученной из сети не совпадают ");
        assertEquals(epic5.getName(), manager.getListFromHashEpic().get(0).getName(), "Поля у задачи " +
                "Epic из менеджера, и полученной из сети не совпадают ");
        assertEquals(subtask3.getName(), manager.getListFromHashSubtask().get(0).getName(), "Поля у задачи " +
                "Task из менеджера, и полученной из сети не совпадают ");
    }

    @Test
    public void checkingThatTheTaskIsUpdated() throws NotFoundException, IOException, InterruptedException {
        manager.addEpic(epic5);
        manager.addTask(task1);
        manager.addSubtask(subtask3);
        String unexpectedTask = manager.getTaskById(task1.getId()).toString();
        String unexpectedSubtask = manager.getSubtaskById(subtask3.getId()).toString();
        task1.setStartTime(task2.getStartTime().plusMinutes(10));
        subtask3.setStartTime(subtask3.getStartTime().plusMinutes(15));
        int idTask = task1.getId();
        int idSubtask = subtask3.getId();
        String task1Json = httpUtils.getGson().toJson(manager.getTaskById(idTask));
        String subtaskJson = httpUtils.getGson().toJson(manager.getSubtaskById(idSubtask));

        HttpResponse<String> task1Response = httpUtils.sendPOSTRequest("http://localhost:8080/tasks" + idTask,
                task1Json);
        HttpResponse<String> subtask3Response = httpUtils.sendPOSTRequest("http://localhost:8080/subtasks"
                + idSubtask, subtaskJson);

        assertEquals(201, task1Response.statusCode(), "код ответа для Task не совпадает");
        assertNotEquals(unexpectedTask, manager.getTaskById(idTask).toString(), "Задачи Task совпадают, " +
                "обновление задачи не произошло");
        assertEquals(201, subtask3Response.statusCode(), "код ответа для Subtask не совпадает");
        assertNotEquals(unexpectedSubtask, manager.getSubtaskById(idSubtask).toString(), "Задачи Subtask " +
                "совпадают, обновление задачи не произошло");
    }

    @Test
    public void checkingThatTheTaskIsNotAddedIfTheTimeIntervalsMatch() throws IOException, InterruptedException {
        manager.addEpic(epic5);
        manager.addTask(task1);
        manager.addSubtask(subtask3);
        task2.setStartTime(task1.getStartTime().plusMinutes(2));
        subtask4.setStartTime(subtask3.getStartTime().plusMinutes(2));
        String task2Json = httpUtils.getGson().toJson(task2);
        String subtask4Json = httpUtils.getGson().toJson(subtask4);

        HttpResponse<String> task2Response = httpUtils.sendPOSTRequest("http://localhost:8080/tasks", task2Json);
        HttpResponse<String> subtask4Response = httpUtils.sendPOSTRequest("http://localhost:8080/subtasks",
                subtask4Json);

        assertEquals(406, task2Response.statusCode(), "Код ответа задания Task не совпадает");
        assertEquals(1, manager.getListFromHashTask().size(), "Некоректное колличество задач Task");
        assertEquals(406, subtask4Response.statusCode(), "Код ответа задания Subtask не совпадает");
        assertEquals(1, manager.getListFromHashSubtask().size(), "Некоректное колличество задач " +
                "Subtask");
    }

    @Test
    public void checkingTheReceiptOfTheTaskListOverTheNetwork() throws IOException, InterruptedException {
        manager.addEpic(epic5);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);

        HttpResponse<String> responseTask = httpUtils.sendGETRequest("http://localhost:8080/tasks/");
        HttpResponse<String> responseSubtask = httpUtils.sendGETRequest("http://localhost:8080/subtasks/");
        HttpResponse<String> responseEpic = httpUtils.sendGETRequest("http://localhost:8080/epics/");

        List<Task> actualListTask = httpUtils.getGson().fromJson(responseTask.body(),
                new TaskListTypeToken().getType());
        List<Subtask> actualListSubtask = httpUtils.getGson().fromJson(responseSubtask.body(),
                new SubtaskListTypeToken().getType());
        List<Epic> actualListEpic = httpUtils.getGson().fromJson(responseEpic.body(),
                new EpicListTypeToken().getType());

        assertEquals(200, responseTask.statusCode(), "Код ответа задания Tфsk не совпадает");
        assertEquals(manager.getListFromHashTask(), actualListTask, "Список заданий Task из менеджера " +
                "не не совпадает со списком заданий полученных из сети");
        assertEquals(200, responseSubtask.statusCode(), "Код ответа задания Subtask не совпадает");
        assertEquals(manager.getListFromHashSubtask(), actualListSubtask, "Список заданий Subtask из менеджера " +
                "не не совпадает со списком заданий полученных из сети");
        assertEquals(200, responseEpic.statusCode(), "Код ответа задания Epic не совпадает");
        assertEquals(manager.getListFromHashEpic(), actualListEpic, "Список заданий Epic из менеджера " +
                "не не совпадает со списком заданий полученных из сети");
    }

    @Test
    public void checkTaskReceiptById() throws IOException, InterruptedException, NotFoundException {
        manager.addEpic(epic5);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);

        HttpResponse<String> responseTask = httpUtils.sendGETRequest("http://localhost:8080/tasks/" +
                task2.getId());
        HttpResponse<String> responseSubtask = httpUtils.sendGETRequest("http://localhost:8080/subtasks/" +
                subtask4.getId());
        HttpResponse<String> responseEpic = httpUtils.sendGETRequest("http://localhost:8080/epics/" +
                epic5.getId());

        Task actualTask = httpUtils.getGson().fromJson(responseTask.body(), Task.class);
        Task actualSubtask = httpUtils.getGson().fromJson(responseSubtask.body(), Subtask.class);
        Task actualEpic = httpUtils.getGson().fromJson(responseEpic.body(), Epic.class);

        assertEquals(200, responseTask.statusCode(), "Код ответа задания Task не совпадает");
        assertEquals(task2, actualTask, "Задание Task полученное из менеджера " +
                "не совпадает с заданием полученным из сети");
        assertEquals(200, responseSubtask.statusCode(), "Код ответа задания Subtask не совпадает");
        assertEquals(subtask4, actualSubtask, "Задание Subtask полученное из менеджера " +
                "не совпадает с заданием полученным из сети");
        assertEquals(200, responseEpic.statusCode(), "Код ответа задания Epic не совпадает");
        assertEquals(epic5, actualEpic, "Задание Epic полученное из менеджера " +
                "не совпадает с заданием полученным из сети");
    }

    @Test
    public void checkTaskDeletionById() throws IOException, InterruptedException {
        manager.addEpic(epic5);
        manager.addTask(task1);
        manager.addSubtask(subtask3);

        HttpResponse<String> responseTask = httpUtils.sendDELETERequest("http://localhost:8080/tasks/",
                task1.getId());
        HttpResponse<String> responseSubtask = httpUtils.sendDELETERequest("http://localhost:8080/subtasks/",
                subtask3.getId());
        HttpResponse<String> responseEpic = httpUtils.sendDELETERequest("http://localhost:8080/epics/",
                epic5.getId());

        assertEquals(200, responseTask.statusCode(), "Код ответа Task не совпадает");
        assertEquals(0, manager.getListFromHashTask().size(), "Список заданий Task после удаления" +
                "не пуст");
        assertEquals(200, responseSubtask.statusCode(), "Код ответа Subtask не совпадает");
        assertEquals(0, manager.getListFromHashSubtask().size(), "Список заданий Subtask после удаления" +
                "не пуст");
        assertEquals(200, responseEpic.statusCode(), "Код ответа Epic не совпадает");
        assertEquals(0, manager.getListFromHashEpic().size(), "Список заданий Epic после удаления" +
                "не пуст");
    }

    @Test
    public void checkGetTheHistoryOverTheNetwork() throws IOException, InterruptedException, NotFoundException {
        manager.addEpic(epic5);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        manager.getTaskById(task1.getId());
        manager.getSubtaskById(subtask3.getId());

        HttpResponse<String> responseTask = httpUtils.sendGETRequest("http://localhost:8080/history");

        List<Task> expectedList = manager.getHistory();
        List<Task> actualListTask = httpUtils.getGson().fromJson(responseTask.body(),
                new TaskListTypeToken().getType());

        assertEquals(200, responseTask.statusCode(), "Код ответа не совпадает");
        assertEquals(expectedList.size(), actualListTask.size(), "Список заданий из менеджа и истоии " +
                "не совпадает");
    }

    @Test
    public void checkToReceiveAListOfTasksSortedByTime() throws NotFoundException, IOException, InterruptedException {
        manager.addEpic(epic5);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        manager.addTask(task1);
        manager.addTask(task2);
        List<Task> expectedList = List.of(manager.getTaskById(task1.getId())
                , manager.getTaskById(task2.getId())
                , manager.getSubtaskById(subtask3.getId())
                , manager.getSubtaskById(subtask4.getId()));

        HttpResponse<String> response = httpUtils.sendGETRequest("http://localhost:8080/prioritized");
        List<Task> actualList = httpUtils.getGson().fromJson(response.body(), new TaskListTypeToken().getType());
        assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        assertEquals(expectedList.size(), actualList.size(), "Список заданий из менеджа и priorirized не совпадают");
    }

    @AfterEach
    public void afterEach() {
        server.stop();
    }
}
