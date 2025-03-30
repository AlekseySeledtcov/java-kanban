import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskIdList;

    public Epic(String name, String description) {
        super(name, description);
        subtaskIdList = new ArrayList<>();
    }

    public void setSubtaskIdList(Integer id) {
        subtaskIdList.add(this.id != id ? id : null);
    }

    public ArrayList<Integer> getSubtaskIdList() {
        return subtaskIdList;
    }

    public void clearSubtaskIdList() {
        subtaskIdList.clear();
    }

    // Удаляет id Сабтаска из списка сабтасков эпика
    public void removeIdStFromEpicArr(int id) {
        for (int i = 0; i < subtaskIdList.size(); i++) {
            if (subtaskIdList.get(i) == id) {
                subtaskIdList.remove(i);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Epic epic = (Epic) o;

        if (id != epic.id) return false;
        if (!Objects.equals(name, epic.name)) return false;
        if (!Objects.equals(description, epic.description)) return false;
        return status == epic.status && subtaskIdList.equals(epic.subtaskIdList);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + subtaskIdList.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%d,%S,%s,%S,%s%n", id, Type.EPIC, name, status, description);
    }
}
