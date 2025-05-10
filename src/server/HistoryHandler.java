package server;

import manager.TaskManager;
import model.Task;

import java.util.List;

class HistoryHandler extends SimpleGetHandler<List<Task>> {
    TaskManager manager;

    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    protected List<Task> getData() {
        return manager.getHistory();
    }
}

