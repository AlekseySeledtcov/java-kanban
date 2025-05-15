package server;

import file.ManagerSaveException;
import file.NotFoundException;
import manager.TaskManager;
import model.Subtask;

import java.util.List;

public class SubtaskHandler extends EntityHandler<Subtask> {

    public SubtaskHandler(TaskManager manager) {
        super(manager, Subtask.class);
    }

    @Override
    protected List<Subtask> getAll() {
        return manager.getListFromHashSubtask();
    }

    @Override
    protected Subtask getById(int id) throws NotFoundException {
        return manager.getSubtaskById(id);
    }

    @Override
    protected void add(Subtask subtask) throws ManagerSaveException {
        manager.addSubtask(subtask);
    }

    @Override
    protected void update(Subtask subtask) throws ManagerSaveException, NotFoundException {
        manager.updateSubtask(subtask);
    }

    @Override
    protected void remove(int id) throws NotFoundException {
        manager.removeSubtaskById(id);
    }
}
