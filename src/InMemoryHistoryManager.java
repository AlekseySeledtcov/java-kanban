import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    MyLinkedHashMap history = new MyLinkedHashMap();

    @Override
    public List<Task> getHistory() {
        return history.getTask();
    }

    @Override
    public void add(Task task) {
        history.linkLast(task);
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }
}
