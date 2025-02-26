import java.util.ArrayList;

public interface TaskManager {
    void addTaskToHash(Task task);
    void addEpicToHash(Epic epic);
    void addSubtaskToHash(Subtask subtask);


    ArrayList<Task> getListFromHashTask();
    ArrayList<Epic> getListFromHashEpic();
    ArrayList<Subtask> getListFromHashSubtask();

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

    ArrayList<Subtask> getEpicSubtask(int id);
}
