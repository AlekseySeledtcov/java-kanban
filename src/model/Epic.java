package model;

import file.Type;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subtaskIdList;

    public Epic(String name, String description) {
        super(name, description);
        subtaskIdList = new ArrayList<>();
        this.duration = Duration.ofMinutes(0);
    }

    public void setSubtaskIdList(Integer id) {
        subtaskIdList.add(this.id != id ? id : null);
    }

    public List<Integer> getSubtaskIdList() {
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
        if (!super.equals(o)) return false;

        Epic epic = (Epic) o;

        return Objects.equals(subtaskIdList, epic.subtaskIdList);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (subtaskIdList != null ? subtaskIdList.hashCode() : 0);
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
