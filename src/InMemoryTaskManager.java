import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    private int id;

    HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        id = 0;
    }

    @Override
    public Task addTask(Task task) {
        task.setId(getId());
        return tasks.put(task.getId(), task);
    }

    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(getId());
        return epics.put(epic.getId(), epic);
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        subtask.setId(getId());
        if (subtask.getEpicId() == subtask.getId()) {
            return null;
        } else {
            epics.get(subtask.getEpicId()).setSubtaskIdList(subtask.getId());
            return subtasks.put(subtask.getId(), subtask);
        }
    }

    // Получение ArrayList по типам задач
    @Override
    public ArrayList<Task> getListFromHashTask() {
        ArrayList<Task> arrTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            arrTasks.add(task);
        }
        return arrTasks;
    }

    @Override
    public ArrayList<Epic> getListFromHashEpic() {
        ArrayList<Epic> arrEpics = new ArrayList<>();
        for (Epic epic : epics.values()) {
            arrEpics.add(epic);
        }
        return arrEpics;
    }

    @Override
    public ArrayList<Subtask> getListFromHashSubtask() {
        ArrayList<Subtask> arrSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            arrSubtasks.add(subtask);
        }
        return arrSubtasks;
    }

    // Удаление задач по типам
    @Override
    public void removeTaskType() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void removeEpicType() {
        ArrayList<Integer> arrIdSubtaskToDel = new ArrayList<>();
        for (Epic epic : epics.values()) {
            arrIdSubtaskToDel.addAll(epic.subtaskIdList);
            historyManager.remove(epic.getId());
        }
        for (Integer id : arrIdSubtaskToDel) {
            subtasks.remove(id);
            historyManager.remove(id);
        }
        epics.clear();
    }

    @Override
    public void removeSubtaskType() {
        ArrayList<Integer> arrIdEpics = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            int epicId = subtask.getEpicId();
            if (!arrIdEpics.contains(epicId)) {
                arrIdEpics.add(epicId);
            }
            historyManager.remove(subtask.getId());
        }
        subtasks.clear();
        if (arrIdEpics.size() != 0) {
            for (Integer arrIdEpic : arrIdEpics) {
                epics.get(arrIdEpic).clearSubtaskIdList();
                epics.get(arrIdEpic).setStatus(Status.NEW);
            }
        }
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    //Обновление задач
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        ArrayList<Integer> idList;
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        idList = epic.getSubtaskIdList();
        epic.setStatus(setEpicStatusById(idList));
    }

    @Override
    public void updateEpic(Epic epic) {
        ArrayList<Integer> arrIdSubtasks;
        Epic oldEpic = epics.get(epic.getId());
        arrIdSubtasks = oldEpic.getSubtaskIdList();
        for (Integer id : arrIdSubtasks) {
            epic.setSubtaskIdList(id);
        }
        epics.put(epic.getId(), epic);
    }

    //Удаление задач по идентификатору.
    @Override
    public void removeTaskById(int id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        ArrayList<Integer> arrIdSubtaskToDel = new ArrayList<>();
        arrIdSubtaskToDel.addAll(epics.get(id).getSubtaskIdList());
        for (Integer key : arrIdSubtaskToDel) {
            subtasks.remove(key);
            historyManager.remove(key);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        int epicId = subtasks.get(id).getEpicId();
        subtasks.remove(id);
        historyManager.remove(id);
        ArrayList<Integer> idList;
        Epic epic = epics.get(epicId);
        epic.removeIdStFromEpicArr(id);
        idList = epic.getSubtaskIdList();
        Status status = setEpicStatusById(idList);
        epic.setStatus(status);
    }

    @Override
    public ArrayList<Subtask> getEpicSubtask(int id) {
        ArrayList<Subtask> arrSb = new ArrayList<>();
        ArrayList<Integer> arrId;
        arrId = epics.get(id).getSubtaskIdList();
        for (Integer key : arrId) {
            arrSb.add(subtasks.get(key));
        }
        return arrSb;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
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

    private int getId() {
        return id++;
    }

}
