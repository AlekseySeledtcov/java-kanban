public class Subtask extends Task {
    private final int epicId;                                    //имя эпика которому принадлежит субтаск

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
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", epicId=" + epicId +
                '}' + "\n";
    }
}
