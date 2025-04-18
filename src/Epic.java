import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIdList;

    public Epic(String name, String description) {
        super(name, description);
        subtaskIdList = new ArrayList<>();
        this.duration = null;
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

    public void setDuration(Duration duration) {
        this.duration = duration;
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
        String s = id + "," + Type.EPIC + "," + name + "," + status + "," + description;
        if (startTime != null) {
            s = s + "," + startTime.format(formatter);
        } else {
            s = s + "," + null;
        }
        if (duration != null) {
            s = s + "," + duration.toMinutes();
        } else {
            s = s + "," + null;
        }
        if (endTime != null) {
            s = s + "," + endTime.format(formatter);
        } else {
            s = s + "," + null;
        }
        s = s + "\n";
        return s;
    }
}
