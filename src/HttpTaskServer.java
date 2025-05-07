import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static InMemoryTaskManager manager;
    private static HttpServer httpServer;
    private static final int PORT = 8080;

    public HttpTaskServer(InMemoryTaskManager manager) {
        HttpTaskServer.manager = manager;
    }

    public static void main(String[] args) throws IOException, NotFoundException {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
    }

    public void start() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/task", new TaskHandler());
        httpServer.createContext("/subtask", new SubtaskHandler());
        httpServer.createContext("/epic", new EpicHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(0);
    }

    static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public static Gson getGson() {
        return gson;
    }

    private static class BaseHttpHandler {
        protected void sendText(HttpExchange exchange, String text, int code) throws IOException {
            byte[] response = text.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(code, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        }

        protected void sendNotFound(HttpExchange exchange) throws IOException {
            byte[] response = "Not fond".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(404, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        }

        protected void sendHasInteractions(HttpExchange exchange) throws IOException {
            byte[] response = "Not Acceptable".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(406, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        }
    }

    class TaskHandler extends BaseHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();

            switch (method) {
                case ("GET"):
                    if (getId(httpExchange) == null) {
                        sendText(httpExchange, serialize(manager.getListFromHashTask()), 200);
                    } else {
                        try {
                            sendText(httpExchange, serialize(manager.getTaskById(getId(httpExchange))), 200);
                        } catch (NotFoundException exception) {
                            System.out.println(exception.getMessage());
                            sendNotFound(httpExchange);
                        }
                    }
                    break;
                case ("POST"):
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
                    if (jsonObject.has("id")) {
                        System.out.println(gson.fromJson(body, Task.class));
                        jsonObject.get("id").getAsInt();
                        try {
                            manager.updateTask(gson.fromJson(body, Task.class));
                        } catch (ManagerSaveException exception) {
                            System.out.println(exception.getMessage());
                            sendHasInteractions(httpExchange);
                            return;
                        } catch (NotFoundException exception) {
                            System.out.println(exception.getMessage());
                            sendNotFound(httpExchange);
                        }
                        sendText(httpExchange, "Задача успешно обновлена", 201);
                    } else {
                        String name = jsonObject.get("name").getAsString();
                        String description = jsonObject.get("description").getAsString();
                        Status status = Status.valueOf(jsonObject.get("status").getAsString());
                        int duration = jsonObject.get("duration").getAsInt();
                        LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime")
                                .getAsString(), Task.formatter);
                        LocalDateTime endTime = LocalDateTime.parse(jsonObject.get("endTime")
                                .getAsString(), Task.formatter);

                        Task task = new Task(name, description, duration);
                        task.setStatus(status);
                        task.setStartTime(startTime);
                        task.setEndTime(endTime);

                        try {
                            manager.addTask(task);
                            sendText(httpExchange, "Задача успешно добавлена", 200);
                        } catch (ManagerSaveException exception) {
                            System.out.println(exception.getMessage());
                            sendHasInteractions(httpExchange);
                            return;
                        }
                    }
                    break;
                case ("DELETE"):
                    try {
                        manager.removeTaskById(getId(httpExchange));
                    } catch (NotFoundException exception) {
                        System.out.println(exception.getMessage());
                        sendNotFound(httpExchange);
                        return;
                    }
                    sendText(httpExchange, "Задача успешно удалена", 200);
                    break;
                case ("PUT"):
                    System.out.println(manager.getTasks());
                    sendText(httpExchange, "Вывод списка тасков", 200);
                    break;
                case ("PATCH"):
                    System.out.println(manager.timedTasks);
                    sendText(httpExchange, "Вывод тасков сортированных по времени", 200);
                default:
                    break;
            }
        }
    }

    static class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();

            switch (method) {
                case ("GET"):
                    if (getId(httpExchange) == null) {
                        sendText(httpExchange, serialize(manager.getListFromHashSubtask()), 200);
                    } else {
                        try {
                            sendText(httpExchange, serialize(manager.getSubtaskById(getId(httpExchange))), 200);
                        } catch (NotFoundException exception) {
                            System.out.println(exception.getMessage());
                            sendNotFound(httpExchange);
                        }
                    }
                    break;
                case ("POST"):
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
                    if (jsonObject.has("id")) {
                        jsonObject.get("id").getAsInt();
                        try {
                            manager.updateSubtask(gson.fromJson(body, Subtask.class));
                        } catch (ManagerSaveException exception) {
                            System.out.println(exception.getMessage());
                            sendHasInteractions(httpExchange);
                            return;
                        } catch (NotFoundException exception) {
                            System.out.println(exception.getMessage());
                            sendNotFound(httpExchange);
                        }
                        sendText(httpExchange, "Задача успешно обновлена", 201);
                    } else {
                        String name = jsonObject.get("name").getAsString();
                        String description = jsonObject.get("description").getAsString();
                        Status status = Status.valueOf(jsonObject.get("status").getAsString());
                        int duration = jsonObject.get("duration").getAsInt();
                        LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime")
                                .getAsString(), Task.formatter);
                        LocalDateTime endTime = LocalDateTime.parse(jsonObject.get("endTime")
                                .getAsString(), Task.formatter);
                        int epicId = jsonObject.get("epicId").getAsInt();

                        Subtask subtask = new Subtask(name, description, epicId, duration);
                        subtask.setStatus(status);
                        subtask.setStartTime(startTime);
                        subtask.setEndTime(endTime);

                        try {
                            manager.addSubtask(subtask);
                            sendText(httpExchange, "Задача успешно добавлена", 200);
                        } catch (ManagerSaveException exception) {
                            System.out.println(exception.getMessage());
                            sendHasInteractions(httpExchange);
                            return;
                        }
                    }
                    break;
                case ("DELETE"):
                    try {
                        manager.removeSubtaskById(getId(httpExchange));
                    } catch (NotFoundException exception) {
                        System.out.println(exception.getMessage());
                        sendNotFound(httpExchange);
                        return;
                    }
                    sendText(httpExchange, "Задача успешно удалена", 200);
                    break;
                default:
                    break;
            }
        }
    }

    static class EpicHandler extends BaseHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();

            switch (method) {
                case ("GET"):
                    String requestPath = httpExchange.getRequestURI().getPath();
                    String[] splitPath = requestPath.split("/");
                    if (splitPath.length == 4 && splitPath[3].equals("subtask")) {
                        try {
                            sendText(httpExchange,
                                    serialize(manager.getEpicById(getId(httpExchange)).getSubtaskIdList()),
                                    200);
                        } catch (NotFoundException exception) {
                            sendNotFound(httpExchange);
                        }

                    } else if (splitPath.length == 3) {
                        try {
                            sendText(httpExchange, serialize(manager.getEpicById(getId(httpExchange))), 200);
                        } catch (NotFoundException exception) {
                            System.out.println(exception.getMessage());
                            sendNotFound(httpExchange);
                        }
                    } else {
                        sendText(httpExchange, serialize(manager.getListFromHashEpic()), 200);
                    }
                    break;
                case ("POST"):
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
                    if (jsonObject.has("id")) {
                        sendHasInteractions(httpExchange);
                    } else {
                        String name = jsonObject.get("name").getAsString();
                        String description = jsonObject.get("description").getAsString();
                        Status status = Status.valueOf(jsonObject.get("status").getAsString());
                        Epic epic = new Epic(name, description);
                        epic.setStatus(status);
                        try {
                            manager.addEpic(epic);
                            sendText(httpExchange, "Задача успешно добавлена", 201);
                        } catch (ManagerSaveException exception) {
                            System.out.println(exception.getMessage());
                            sendHasInteractions(httpExchange);
                            return;
                        }
                    }
                    break;
                case ("DELETE"):
                    try {
                        manager.removeEpicById(getId(httpExchange));
                    } catch (NotFoundException exception) {
                        System.out.println(exception.getMessage());
                        sendNotFound(httpExchange);
                        return;
                    }
                    sendText(httpExchange, "Задача успешно удалена", 200);
                    break;
                default:
                    break;
            }
        }
    }

    static class HistoryHandler extends BaseHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();

            switch (method) {
                case ("GET"):
                    sendText(httpExchange, serialize(manager.getHistory()), 200);
                    break;
                default:
                    break;
            }
        }
    }

    static class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            switch (method) {
                case ("GET"):
                    sendText(httpExchange, serialize(manager.timedTasks), 200);
                    break;
                default:
                    break;
            }
        }
    }

    private static Integer getId(HttpExchange exchange) {
        String requestPath = exchange.getRequestURI().getPath();
        String[] splitPath = requestPath.split("/");
        if (splitPath.length == 2) {
            return null;
        } else {
            return Integer.parseInt(splitPath[2]);
        }
    }

    private static String serialize(Object task) {
        return gson.toJson(task);
    }

    private static class DurationAdapter extends TypeAdapter<Duration> {
        @Override
        public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
            if (duration == null) {
                jsonWriter.nullValue();
            } else {
                jsonWriter.value(duration.toMinutes());
            }
        }

        @Override
        public Duration read(final JsonReader jsonReader) throws IOException {
            Long dateSting = jsonReader.nextLong();
            if (dateSting.equals("null")) {
                return null;
            } else {
                return Duration.ofMinutes(dateSting);
            }
        }
    }

    private static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
            if (localDateTime == null) {
                jsonWriter.nullValue();
            } else {
                jsonWriter.value(localDateTime.format(Task.formatter));
            }
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            String dateString = jsonReader.nextString();
            if ("null".equals(dateString)) {
                return null;
            } else {
                return LocalDateTime.parse(dateString, Task.formatter);
            }
        }
    }
}
