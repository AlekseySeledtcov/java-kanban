import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public void addTask(Task task) {
        task.setId(getId());
        intersection(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(getId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(getId());
        if (subtask.getEpicId() == subtask.getId()) {
            return;
        }
        epics.get(subtask.getEpicId()).setSubtaskIdList(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        epicStartTimeCalculator(epics.get(subtask.getEpicId()));
        epicDurationUpdate(subtask);
        epicEndTimeCalculator(epics.get(subtask.getEpicId()));
    }

    // Получение списка по типам задач

    @Override
    public List<Task> getListFromHashTask() {
        List<Task> arrTasks = new ArrayList<>();
        arrTasks.addAll(tasks.values());
        return arrTasks;
    }

    @Override
    public List<Epic> getListFromHashEpic() {
        List<Epic> arrEpics = new ArrayList<>();
        arrEpics.addAll(epics.values());
        return arrEpics;
    }

    @Override
    public List<Subtask> getListFromHashSubtask() {
        List<Subtask> arrSubtasks = new ArrayList<>();
        arrSubtasks.addAll(subtasks.values());
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
            arrIdSubtaskToDel.addAll(epic.getSubtaskIdList());
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
            arrIdEpics.stream()
                    .map(id -> {
                        epics.get(id).clearSubtaskIdList();
                        epics.get(id).setStatus(Status.NEW);
                        epics.get(id).setStartTime(null);
                        epics.get(id).setDuration(null);
                        epics.get(id).setEndTime(null);
                        return id;
                    })
                    .collect(Collectors.toList());
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
        epicDurationUpdate(subtask);
        epicStartTimeCalculator(epics.get(subtask.getEpicId()));
        epicEndTimeCalculator(epics.get(subtask.getEpicId()));
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
        epics.get(epicId).setDuration(epics.get(epicId).getDuration().minus(subtasks.get(id).getDuration()));
        subtasks.remove(id);
        historyManager.remove(id);
        ArrayList<Integer> idList;
        Epic epic = epics.get(epicId);
        epic.removeIdStFromEpicArr(id);
        idList = epic.getSubtaskIdList();
        Status status = setEpicStatusById(idList);
        epic.setStatus(status);
        epicStartTimeCalculator(epics.get(epicId));
        epicEndTimeCalculator(epics.get(epicId));
    }

    @Override
    public List<Subtask> getEpicSubtask(int id) {
        List<Subtask> arrSb = epics.get(id).getSubtaskIdList().stream()
                .map(key -> subtasks.get(key))
                .collect(Collectors.toList());
        return arrSb;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public Set<Task> getPrioritizedTasks() {
        Set<Task> timedTasks;
        List<Task> allTasks = getListFromHashTask();
        allTasks.addAll(getListFromHashSubtask());
        allTasks.addAll(getListFromHashEpic());
        timedTasks = allTasks.stream()
                .filter(task -> task.getStartTime() != null)
                .collect(Collectors.toSet());
        return timedTasks;
    }

    @Override
    public boolean intersection(Task task) {
        boolean bol = getPrioritizedTasks().stream()
                .anyMatch(s -> s.intersectionCheck(task) == true);
        return bol;
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

    private void epicDurationUpdate(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (subtask.getStatus().equals(Status.NEW) || subtask.getStatus().equals(Status.IN_PROGRESS)) {
            if (epic.duration == (null)) {
                epic.setDuration(subtask.getDuration());
            } else {
                epic.setDuration(epic.getDuration().plus(subtask.getDuration()));
            }
        } else if (subtask.getStatus().equals(Status.DONE)) {
            epic.setDuration(epic.getDuration().minus(subtask.getDuration()));
        }
    }

    private void epicStartTimeCalculator(Epic epic) {
        LocalDateTime startTime;
        Optional<LocalDateTime> oStartTime = epic.getSubtaskIdList().stream()
                .map(id -> subtasks.get(id).getStartTime())
                .min(LocalDateTime::compareTo);
        if (oStartTime.isPresent()) {
            startTime = oStartTime.get();
        } else {
            startTime = null;
        }
        epic.setStartTime(startTime);
    }

    private void epicEndTimeCalculator(Epic epic) {
        LocalDateTime endTime;
        Optional<LocalDateTime> oEndTime = epic.getSubtaskIdList().stream()
                .map(id -> subtasks.get(id).getEndTime())
                .max(LocalDateTime::compareTo);
        if (oEndTime.isPresent()) {
            endTime = oEndTime.get();
        } else {
            endTime = null;
        }
        epic.setEndTime(endTime);
    }
}
