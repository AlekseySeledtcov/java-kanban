import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private int size;
    private Map<Integer, Node> removeMap;

    public InMemoryHistoryManager() {
        head = null;
        tail = null;
        size = 0;
        removeMap = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        Node newNode = new Node(task);
        if (removeMap.containsKey(task.getId())) {
            removeNode(removeMap.get(task.getId()));
            removeMap.remove(task.getId());
        }
        removeMap.put(task.getId(), newNode);

        if (head != null && tail != null && head.equals(tail)) {
            tail = newNode;
            tail.prev = head;
            head.next = tail;
        } else if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        Node nextNode = head;
        if (nextNode != null) {
            for (int i = 0; i < size; i++) {
                historyList.add(nextNode.getData());
                nextNode = nextNode.next;
            }
        }
        return historyList;
    }

    @Override
    public boolean remove(int id) {
        boolean isRemoved = false;
        if (removeMap.containsKey(id)) {
            removeNode(removeMap.get(id));
            removeMap.remove(id);
            isRemoved = true;
        }
        return isRemoved;
    }

    private void removeNode(Node node) {
        if (node.prev == null) {
            head = node.next;
            head.prev = null;
        } else if (node.next == null) {
            tail = node.prev;
            tail.next = null;
        } else {
            Node head = node.prev;
            Node tail = node.next;
            head.next = tail;
            tail.prev = head;
        }
        size--;
    }
}
