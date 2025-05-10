package server;


import manager.TaskManager;
import model.Task;

import java.util.Set;

class PrioritizedHandler extends SimpleGetHandler<Set<Task>> {
    TaskManager manager;

    public PrioritizedHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    protected Set<Task> getData() {
        return manager.getTimedTasks();
    }
}

