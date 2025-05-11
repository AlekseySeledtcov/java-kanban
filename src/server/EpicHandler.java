package server;

import com.sun.net.httpserver.HttpExchange;
import file.HttpMethod;
import file.ManagerSaveException;
import file.NotFoundException;
import manager.TaskManager;
import model.Epic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends EntityHandler<Epic> {

    public EpicHandler(TaskManager manager) {
        super(manager, Epic.class);
    }

    @Override
    protected List<Epic> getAll() {
        return manager.getListFromHashEpic();
    }

    @Override
    protected Epic getById(int id) throws NotFoundException {
        return manager.getEpicById(id);
    }

    @Override
    protected void add(Epic epic) throws ManagerSaveException {
        manager.addEpic(epic);
    }

    @Override
    protected void update(Epic epic) throws ManagerSaveException {
        manager.updateEpic(epic);
    }

    @Override
    protected void remove(int id) throws NotFoundException {
        manager.removeEpicById(id);
    }

    private List<Integer> getEpicIdList(int id) throws NotFoundException {
        return manager.getEpicById(id).getSubtaskIdList();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpMethod method = HttpMethod.valueOf(exchange.getRequestMethod());
        switch (method) {
            case GET:
                if (getId(exchange) == null) {
                    sendText(exchange, serialize(getAll()), 200);
                } else {
                    try {
                        sendText(exchange, serialize(getById(getId(exchange))), 200);
                    } catch (NotFoundException e) {
                        sendNotFound(exchange);
                    }
                }
                break;
            case POST:
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                if (exchange.getRequestURI().getPath().contains("/subtasks") &&
                        exchange.getRequestURI().getPath().contains("/epics")) {
                    try {
                        sendText(exchange, getEpicIdList(getId(exchange)).toString(), 200);
                    } catch (Exception e) {
                        sendNotFound(exchange);
                    }
                } else if (body.contains("\"id\"")) {
                    try {
                        update(fromJson(body));
                        sendText(exchange, "Задача успешно обновлена", 201);
                    } catch (Exception e) {
                        sendNotFound(exchange);
                    }
                } else {
                    try {
                        add(fromJson(body));
                        sendText(exchange, "Задача успешно добавлена", 201);
                    } catch (Exception e) {
                        sendHasInteractions(exchange);
                    }
                }
                break;
            case DELETE:
                try {
                    remove(getId(exchange));
                    sendText(exchange, "Задача успешно удалена", 200);
                } catch (Exception e) {
                    sendNotFound(exchange);
                }
                break;
        }
    }
}
