import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskIdList;

    public Epic(String name, String description) {
        super(name, description);
        subtaskIdList = new ArrayList<>();
    }

    public void setSubtaskIdList(Integer id) {
        subtaskIdList.add(id);
    }

    public ArrayList<Integer> getSubtaskIdList() {
        return subtaskIdList;
    }

    public void clearSubtaskIdList () {
    subtaskIdList.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtaskIdList=" + subtaskIdList +
                '}' + "\n";
    }
}
