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
import model.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class EpicHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager manager;

    public EpicHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpMethod method = HttpMethod.valueOf(httpExchange.getRequestMethod());

        switch (method) {
            case GET:
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
            case POST:
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
            case DELETE:
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
