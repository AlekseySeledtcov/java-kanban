package manager;

import file.NotFoundException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    Set<Task> getTimedTasks();

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    List<Task> getListFromHashTask();

    List<Epic> getListFromHashEpic();

    List<Subtask> getListFromHashSubtask();

    void removeTaskType();

    void removeEpicType();

    void removeSubtaskType();

    Task getTaskById(int id) throws NotFoundException;

    Epic getEpicById(int id) throws NotFoundException;

    Subtask getSubtaskById(int id) throws NotFoundException;

    void updateTask(Task task) throws NotFoundException;

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask) throws NotFoundException;

    void removeTaskById(int id) throws NotFoundException;

    void removeEpicById(int id) throws NotFoundException;

    void removeSubtaskById(int id) throws NotFoundException;

    List<Subtask> getEpicSubtask(int id);

    List<Task> getHistory();

    boolean intersection(Task task);
}
