package server;

import com.sun.net.httpserver.HttpExchange;
import file.ManagerSaveException;
import file.NotFoundException;
import manager.TaskManager;
import model.Epic;

import java.io.IOException;
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

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        if (getId(exchange) != null &&
                exchange.getRequestURI().getPath().contains("subtasks")) {
            try {
                sendText(exchange, getEpicIdList(getId(exchange)).toString(), 200);
            } catch (Exception e) {
                sendNotFound(exchange);
            }
        } else if (getId(exchange) == null) {
            sendText(exchange, serialize(getAll()), 200);
        } else {
            try {
                sendText(exchange, serialize(getById(getId(exchange))), 200);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            }
        }
    }

    private List<Integer> getEpicIdList(int id) throws NotFoundException {
        return manager.getEpicById(id).getSubtaskIdList();
    }
}
