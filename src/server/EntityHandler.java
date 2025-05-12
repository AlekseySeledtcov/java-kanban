package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import file.HttpMethod;
import file.ManagerSaveException;
import file.NotFoundException;
import manager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class EntityHandler<T> extends BaseHttpHandler implements HttpHandler { // Класс абстрактный и типизированный, чтобы работать с любым типом задач
    protected final TaskManager manager;
    private final Class<T> type;

    public EntityHandler(TaskManager manager, Class<T> type) {
        this.manager = manager;
        this.type = type;
    }

    // Универсальный способ превратить JSON в объект нужного типа
    protected T fromJson(String body) {
        return gson.fromJson(body, type);
    }

    // Эти методы ты сам реализуешь в наследниках
    protected abstract List<T> getAll();

    protected abstract T getById(int id) throws NotFoundException;

    protected abstract void add(T entity) throws ManagerSaveException;

    protected abstract void update(T entity) throws ManagerSaveException, NotFoundException;

    protected abstract void remove(int id) throws NotFoundException;

    protected void handleGet(HttpExchange exchange) throws IOException {
        if (getId(exchange) == null) {
            sendText(exchange, serialize(getAll()), 200);
        } else {
            try {
                sendText(exchange, serialize(getById(getId(exchange))), 200);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            }
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpMethod method = HttpMethod.valueOf(exchange.getRequestMethod());
        switch (method) {
            case GET:
                handleGet(exchange);
                break;
            case POST:
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                if (body.contains("\"id\"")) {
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