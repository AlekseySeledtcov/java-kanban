import java.util.List;

public interface TaskManager {
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    List<Task> getListFromHashTask();

    List<Epic> getListFromHashEpic();

    List<Subtask> getListFromHashSubtask();

    void removeTaskType();

    void removeEpicType();

    void removeSubtaskType();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

    List<Subtask> getEpicSubtask(int id);

    List<Task> getHistory();

    boolean intersection(Task task);
}
