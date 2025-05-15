package server;

import file.ManagerSaveException;
import file.NotFoundException;
import manager.TaskManager;
import model.Task;

import java.util.List;

public class TaskHandler extends EntityHandler<Task> {

    public TaskHandler(TaskManager manager) {
        super(manager, Task.class); // подставляем тип Task
    }

    @Override
    protected List<Task> getAll() {
        return manager.getListFromHashTask();
    }

    @Override
    protected Task getById(int id) throws NotFoundException {
        return manager.getTaskById(id);
    }

    @Override
    protected void add(Task task) throws ManagerSaveException {
        manager.addTask(task);
    }

    @Override
    protected void update(Task task) throws ManagerSaveException, NotFoundException {
        manager.updateTask(task);
    }

    @Override
    protected void remove(int id) throws NotFoundException {
        manager.removeTaskById(id);
    }
}
