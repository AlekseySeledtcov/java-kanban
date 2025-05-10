package server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import file.HttpMethod;
import file.ManagerSaveException;
import file.NotFoundException;
import file.Status;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

class TaskHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager manager;

    public TaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpMethod method = HttpMethod.valueOf(httpExchange.getRequestMethod());

        switch (method) {
            case GET:
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
            case POST:
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
                if (jsonObject.has("id")) {
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
                        sendText(httpExchange, "Задача успешно добавлена", 201);
                    } catch (ManagerSaveException exception) {
                        System.out.println(exception.getMessage());
                        sendHasInteractions(httpExchange);
                        return;
                    }
                }
                break;
            case DELETE:
                try {
                    manager.removeTaskById(getId(httpExchange));
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
