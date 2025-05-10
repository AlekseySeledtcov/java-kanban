package model;

import file.Type;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, String description, int epicId, int durationInMinutes) {
        super(name, description, durationInMinutes);
        this.epicId = epicId;
    }

    public int getId() {
        return id;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Subtask subtask = (Subtask) o;

        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + epicId;
        return result;
    }

    @Override
    public String toString() {
        return String.format("%d,%S,%s,%S,%s,%s,%d,%s,%d%n", id, Type.SUBTASK, name, status, description,
                startTime.format(formatter), duration.toMinutes(), endTime.format(formatter), epicId);
    }
}
