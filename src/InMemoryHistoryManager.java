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

        if (head == null && tail == null) {
            head = newNode;
        } else if (head != null && tail == null) {
            tail = newNode;
            tail.prev = head;
            head.next = tail;
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
        if (node.prev != null && node.next != null) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        } else if (node.next == null && node.prev != null) {
            tail = node.prev;
            tail.next = null;
        } else if (node.prev == null & node.next != null) {
            head = node.next;
            head.prev = null;
        } else {
            head = null;
            tail = null;
        }
        size--;
    }

}
