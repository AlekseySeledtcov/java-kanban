import java.util.ArrayList;
import java.util.List;
public class InMemoryHistoryManager implements HistoryManager {

    private int memorySize;
    private List<Task> history;

    public InMemoryHistoryManager() {
        memorySize = 10;
        history = new ArrayList<>(memorySize);
    }
    public List<Task> getHistory() {
        return history;
    }

    public void add(Task task) {
        if (history.size() == memorySize) {
            history.remove(0);
        }
        history.add(task);
    }
}
