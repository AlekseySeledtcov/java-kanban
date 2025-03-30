import java.util.Objects;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public int getId() {
        return id;
    }

    public int getEpicId() {
        return epicId;
    }

    public Status getStatus() {
        return status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subtask subtask = (Subtask) o;

        if (id != subtask.id) return false;
        if (!Objects.equals(name, subtask.name)) return false;
        if (!Objects.equals(description, subtask.description)) return false;
        return status == subtask.status && epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + epicId;
        return result;
    }

    @Override
    public String toString() {
        return String.format("%d,%S,%s,%S,%s,%s%n", id, Type.SUBTASK, name, status, description, epicId);
    }
}
