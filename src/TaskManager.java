import java.util.ArrayList;

public interface TaskManager {
    Task addTask(Task task);
    Epic addEpic(Epic epic);
    Subtask addSubtask(Subtask subtask);


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
