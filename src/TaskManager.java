import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    private int id;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        id = 0;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public int getId() {
        id++;
        return id;
    }

    public void addTaskToHash(Task task) {
        int key;
        do {
            key = getId();
        } while (tasks.containsKey(key));
        task.setId(key);
        tasks.put(key, task);
    }

    public void addEpicToHash(Epic epic) {
        int key;
        do {
            key = getId();
        } while (epics.containsKey(key));
        epic.setId(key);
        epics.put(key, epic);
    }

    public void addSubtaskToHash(Subtask subtask) {
        int key;
        do {
            key = getId();
        } while (subtasks.containsKey(key));
        int iDFromEpic = subtask.getEpicId();
        Epic epic = epics.get(iDFromEpic);
        epic.setSubtaskIdList(key);
        subtask.setId(key);
        subtasks.put(key, subtask);
    }

    // Получение ArrayList по типам задач
    public ArrayList<Task> getListFromHashTask() {
        ArrayList<Task> arrTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            arrTasks.add(task);
        }
        return arrTasks;
    }

    public ArrayList<Epic> getListFromHashEpic() {
        ArrayList<Epic> arrEpics = new ArrayList<>();
        for (Epic epic : epics.values()) {
            arrEpics.add(epic);
        }
        return arrEpics;
    }

    public ArrayList<Subtask> getListFromHashSubtask() {
        ArrayList<Subtask> arrSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            arrSubtasks.add(subtask);
        }
        return arrSubtasks;
    }

    // Удаление задач по типам
    public void removeTaskType() {
        tasks.clear();
    }

    public void removeEpicType() {
        ArrayList<Integer> arrIdSubtaskToDel = new ArrayList<>();
        for (Epic epic : epics.values()) {
            arrIdSubtaskToDel.addAll(epic.subtaskIdList);
        }
        for (Integer id : arrIdSubtaskToDel) {
            subtasks.remove(id);
        }
        epics.clear();
    }

    public void removeSubtaskType() {
        ArrayList<Integer> arrIdEpics = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            int epicId = subtask.getEpicId();
            if (!arrIdEpics.contains(epicId)) {
                arrIdEpics.add(epicId);
            }
        }
        subtasks.clear();
        if (arrIdEpics.size() != 0) {
            Epic epic;
            for (Integer arrIdEpic : arrIdEpics) {
                epic = epics.get(arrIdEpic);
                epic.clearSubtaskIdList();
                epic.setStatus(Status.NEW);
            }
        }
    }

    //Получение объекта по идентификатору
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    //Обновление задач
    public void updateTask(Task task) {
        int key = task.getId();
        tasks.remove(key);
        tasks.put(key, task);
    }

    public void updateSubtask(Subtask subtask) {
        int key = subtask.getId(); // id переданный таски
        int epicId = subtask.getEpicId();
        ArrayList<Integer> idList;  //список связанных с эпиком сабтасков
        subtasks.remove(key);
        subtasks.put(key, subtask);
        Epic epic = epics.get(epicId);
        idList = epic.getSubtaskIdList();
        epic.setStatus(setEpicStatusById(idList));
    }

    //Получение списка всех подзадач определённого эпика
    public ArrayList<Subtask> getListSubtasksFromEpicId(Integer epicId) {
        ArrayList<Integer> arrId;
        Epic epic = epics.get(epicId);
        arrId = epic.getSubtaskIdList();
        ArrayList<Subtask> st = new ArrayList<>();
        for (Integer integer : arrId) {
            st.add(subtasks.get(integer));
        }
        return st;
    }

    //Удаление задач по идентификатору.
    public void removeTaskById(int id) {
            tasks.remove(id);
    }

    public void removeEpicById(int id) {
        ArrayList<Integer> arrIdSubtaskToDel = new ArrayList<>();
        Epic epic = epics.get(id);
        arrIdSubtaskToDel.addAll(epic.getSubtaskIdList());
        for (Integer key : arrIdSubtaskToDel) {
            subtasks.remove(key);
        }
        epics.remove(id);
    }

    public void removeSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        int epicId = subtask.getEpicId();
        subtasks.remove(id);
        ArrayList<Integer> idList;
        Epic epic = epics.get(epicId);
        idList = epic.getSubtaskIdList();
    }

    // Расчитывает статус Эпика
    private Status setEpicStatusById(ArrayList<Integer> idList) {
        Status status = Status.IN_PROGRESS;
        if (idList.size() == 0) {
            return Status.NEW;
        } else {
            int nNew = 0;
            int nDone = 0;
            for (int i = 0; i < idList.size(); i++) {
                int key = idList.get(i);
                Status st = subtasks.get(key).getStatus();
                if (st == Status.NEW) {
                    nNew++;
                }
                if (st == Status.DONE) {
                    nDone++;
                }
            }
            if (nNew == idList.size()) {
                status = Status.NEW;
            }
            if (nDone == idList.size()) {
                status = Status.DONE;
            }
        }
        return status;
    }
}
