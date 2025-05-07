import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    HttpTaskServer server;
    InMemoryTaskManager manager;
    Gson gson = HttpTaskServer.getGson();
    Task task1;
    Task task2;
    Subtask subtask3;
    Subtask subtask4;
    Epic epic5;

    class TaskListTypeToken extends TypeToken<List<Task>> {
    }

    class SubtaskListTypeToken extends TypeToken<List<Subtask>> {
    }

    class EpicListTypeToken extends TypeToken<List<Epic>> {
    }

    class IntegerListTypeToken extends TypeToken<List<Integer>> {
    }

    @BeforeEach
    void beforeEach() throws IOException {
        manager = new InMemoryTaskManager();
        server = new HttpTaskServer(manager);
        server.start();
        Gson gson = HttpTaskServer.getGson();

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
    public void checkingThatTheTaskTaskIsAdded() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/task");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> taskList = manager.getListFromHashTask();

        assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        assertNotNull(taskList, "Задачи не возвращаются");
        assertEquals(1, taskList.size(), "Некоректное колличество задач");
        assertEquals("ИмяTask1", taskList.get(0).getName());
    }

    @Test
    public void checkingThatTheTaskIsNotAddedIfTheTimeIntervalsMatch() throws IOException, InterruptedException {
        manager.addTask(task1);
        task2.setStartTime(LocalDateTime.parse("2025.05.05 12:03", Task.formatter));
        task2.setEndTime(task1.getStartTime().plusMinutes(15));
        String task2Json = gson.toJson(task2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/task");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(task2Json))
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode(), "Код ответа не совпадает");
        assertEquals(1, manager.getListFromHashTask().size(), "Некоректное колличество задач");

    }

    @Test
    public void checkingThatTheTaskIsUpdated() throws NotFoundException, IOException, InterruptedException {
        manager.addTask(task1);
        manager.addTask(task2);
        String unexpected = manager.getTaskById(task2.getId()).toString();
        task2.setStartTime(task2.getStartTime().plusMinutes(10));
        String task2Json = gson.toJson(manager.getTaskById(task2.getId()));
        int id = task2.getId();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/task/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(task2Json))
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String actual = manager.getTaskById(id).toString();

        assertEquals(201, response.statusCode(), "код ответа не совпадает");
        assertNotEquals(unexpected, actual, "Задания совпадают, обновление задания не произошло");
    }

    @Test
    public void checkingTheReceiptOfTheTaskListOverTheNetwork() throws IOException, InterruptedException {
        manager.addTask(task1);
        manager.addTask(task2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> expectedList = manager.getListFromHashTask();
        List<Task> actualList = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        assertEquals(expectedList, actualList, "Список заданий из менеджера не равен списку полученному " +
                "из сети");
    }

    @Test
    public void checkTaskReceiptById() throws IOException, InterruptedException, NotFoundException {
        manager.addTask(task1);
        manager.addTask(task2);
        HttpClient client = HttpClient.newHttpClient();
        int id = task1.getId();
        URI url = URI.create("http://localhost:8080/task/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task expectedTask = manager.getTaskById(id);
        Task actualTask = gson.fromJson(response.body(), Task.class);

        assertEquals(expectedTask, actualTask, "Задания полученные из менеджера и по сети не совпадают");
        assertEquals(200, response.statusCode(), "Код ответа не совпадает");
    }

    @Test
    public void checkTaskDeletionById() throws IOException, InterruptedException {
        manager.addTask(task1);
        manager.addTask(task2);
        List<Task> unexpectedList = manager.getListFromHashTask();
        HttpClient client = HttpClient.newHttpClient();
        int id = task1.getId();
        URI url = URI.create("http://localhost:8080/task/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> actualList = manager.getListFromHashTask();

        assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        assertNotEquals(unexpectedList, actualList, "Список заданий до удаления совпал со списком заданий" +
                "после удаления");
    }

    @Test
    public void checkingTheReceiptOfTheSubtaskListOverTheNetwork() throws IOException, InterruptedException {
        manager.addEpic(epic5);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> expectedList = manager.getListFromHashSubtask();
        List<Subtask> actualList = gson.fromJson(response.body(), new SubtaskListTypeToken().getType());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        assertEquals(expectedList, actualList, "Список заданий из менеджера не равен списку полученному " +
                "из сети");
    }

    @Test
    public void checkSubtaskReceiptById() throws IOException, InterruptedException, NotFoundException {
        manager.addEpic(epic5);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        HttpClient client = HttpClient.newHttpClient();
        int id = subtask3.getId();
        URI url = URI.create("http://localhost:8080/subtask/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask expectedSubtask = manager.getSubtaskById(id);
        Subtask actualSubtask = gson.fromJson(response.body(), Subtask.class);

        assertEquals(expectedSubtask, actualSubtask, "Задания полученные из менеджера и по сети не совпадают");
        assertEquals(200, response.statusCode(), "Код ответа не совпадает");
    }

    @Test
    public void checkingThatTheTaskSubtaskIsAdded() throws IOException, InterruptedException {
        manager.addEpic(epic5);
        String subtaskJson = gson.toJson(subtask3);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> subTaskList = manager.getListFromHashSubtask();

        assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        assertNotNull(subTaskList, "Задачи не возвращаются");
        assertEquals(1, subTaskList.size(), "Некоректное колличество задач");
        assertEquals("ИмяSubtask3", subTaskList.get(0).getName());
    }

    @Test
    public void checkingThatTheSubtaskIsNotAddedIfTheTimeIntervalsMatch() throws IOException, InterruptedException {
        manager.addEpic(epic5);
        manager.addSubtask(subtask3);
        subtask4.setStartTime(LocalDateTime.parse("2025.05.05 13:03", Task.formatter));
        subtask4.setEndTime(subtask3.getStartTime().plusMinutes(15));
        String subtask4Json = gson.toJson(subtask4);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtask4Json))
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode(), "Код ответа не совпадает");
        assertEquals(1, manager.getListFromHashSubtask().size(), "Некоректное колличество задач");

    }

    @Test
    public void checkingThatTheSubtaskIsUpdated() throws NotFoundException, IOException, InterruptedException {
        manager.addEpic(epic5);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        String unexpected = manager.getSubtaskById(subtask4.getId()).toString();
        subtask4.setStartTime(subtask4.getStartTime().plusMinutes(10));
        String subtask4Json = gson.toJson(manager.getSubtaskById(subtask4.getId()));
        int id = subtask4.getId();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtask/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtask4Json))
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String actual = manager.getSubtaskById(id).toString();

        assertEquals(201, response.statusCode(), "код ответа не совпадает");
        assertNotEquals(unexpected, actual, "Задания совпадают, обновление задания не произошло");
    }

    @Test
    public void checkSubtaskDeletionById() throws IOException, InterruptedException {
        manager.addEpic(epic5);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        List<Subtask> unexpectedList = manager.getListFromHashSubtask();
        HttpClient client = HttpClient.newHttpClient();
        int id = subtask3.getId();
        URI url = URI.create("http://localhost:8080/subtask/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> actualList = manager.getListFromHashSubtask();

        assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        assertNotEquals(unexpectedList, actualList, "Список заданий до удаления совпал со списком заданий" +
                "после удаления");
    }

    @Test
    public void checkingTheReceiptOfTheEpicListOverTheNetwork() throws IOException, InterruptedException {
        manager.addEpic(epic5);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> expectedList = manager.getListFromHashEpic();
        List<Epic> actualList = gson.fromJson(response.body(), new EpicListTypeToken().getType());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        assertEquals(expectedList, actualList, "Список заданий из менеджера не равен списку полученному " +
                "из сети");
    }

    @Test
    public void checkEpicReceiptById() throws IOException, InterruptedException, NotFoundException {
        manager.addEpic(epic5);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        HttpClient client = HttpClient.newHttpClient();
        int id = epic5.getId();
        URI url = URI.create("http://localhost:8080/epic/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic expectedEpic = manager.getEpicById(id);
        Epic actualEpic = gson.fromJson(response.body(), Epic.class);

        assertEquals(expectedEpic, actualEpic, "Задания полученные из менеджера и по сети не совпадают");
        assertEquals(200, response.statusCode(), "Код ответа не совпадает");
    }

    @Test
    public void CheckingThatEpicHasBeenAdded() throws IOException, InterruptedException {
        String epicJson = gson.toJson(epic5);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> epicList = manager.getListFromHashEpic();

        assertEquals(201, response.statusCode(), "Код ответа не совпадает");
        assertNotNull(epicList, "Задачи не возвращаются");
        assertEquals(1, epicList.size(), "Некоректное колличество задач");
        assertEquals("ИмяEpic5", epicList.get(0).getName());
    }

    ////////////////////////
    @Test
    public void checkEpicDeletionById() throws IOException, InterruptedException {
        manager.addEpic(epic5);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        List<Epic> unexpectedList = manager.getListFromHashEpic();
        HttpClient client = HttpClient.newHttpClient();
        int id = epic5.getId();
        URI url = URI.create("http://localhost:8080/epic/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> actualList = manager.getListFromHashEpic();

        assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        assertNotEquals(unexpectedList, actualList, "Список заданий до удаления совпал со списком заданий" +
                "после удаления");
    }

    @Test
    public void CheckGetTheHistoryOverTheNetwork() throws IOException, InterruptedException, NotFoundException {
        manager.addEpic(epic5);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        List<Task> expectedList = List.of(manager.getTaskById(task1.getId()), manager.getSubtaskById(subtask3.getId()));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> actualList = manager.getHistory();

        assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        assertEquals(expectedList, actualList, "Список заданий из менеджа и истоии не совпадает");
    }

    @Test
    public void CheckToReceiveAListOfTasksSortedByTime() throws NotFoundException, IOException, InterruptedException {
        manager.addEpic(epic5);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        manager.addTask(task1);
        manager.addTask(task2);
        List<Task> expectedList = List.of(manager.getTaskById(task1.getId())
                , manager.getTaskById(task2.getId())
                , manager.getSubtaskById(subtask3.getId())
                , manager.getSubtaskById(subtask4.getId()));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> actualList = manager.timedTasks.stream()
                .collect(Collectors.toList());
        System.out.println(actualList);
        assertEquals(200, response.statusCode(), "Код ответа не совпадает");
        assertEquals(expectedList, actualList, "Список заданий из менеджа и priorirized не совпадают");
    }

    @AfterEach
    void afterEach() {
        server.stop();
    }
}