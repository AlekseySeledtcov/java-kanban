import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, Subtask> subtasks;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    int id = 0;
    public void addTaskToHash(Task task) {
        do {
            id++;
        } while (tasks.containsKey(id));
        task.setId(id);
        tasks.put(id, task);
    }

    public void addEpicToHash(Epic epic) {
        do {
            id++;
        } while (epics.containsKey(id));
        epic.setId(id);
        epics.put(id, epic);
    }

    public void addSubtaskToHash(Subtask subtask) {
        do {
            id++;
        } while (subtasks.containsKey(id));
        int iDFromEpic = subtask.getEpicId();
        Epic epic = epics.get(iDFromEpic);
        epic.setSubtaskIdList(id);
        subtask.setId(id);
        subtasks.put(id, subtask);
    }

    // Получение ArrayList по типам задач
    public ArrayList<Task> getListFromHashTask(HashMap<Integer, Task> tasks) {
        ArrayList<Task> arrTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            arrTasks.add(task);

        }
        return arrTasks;
    }

    public ArrayList<Epic> getListFromHashEpic(HashMap<Integer, Epic> epics) {
        ArrayList<Epic> arrEpics = new ArrayList<>();
        for (Epic epic : epics.values()) {
            arrEpics.add(epic);
        }
        return arrEpics;
    }

    public ArrayList<Subtask> getListFromHashSubtask(HashMap<Integer, Subtask> subtasks) {
        ArrayList<Subtask> arrSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            arrSubtasks.add(subtask);
        }
        return arrSubtasks;
    }

    // Удаление задач по типам
    public void removeTaskType(HashMap<Integer, Task> tasks) {
        tasks.clear();
    }

    public void removeEpicType(HashMap<Integer, Epic> epics) {
        epics.clear();
    }

    public void removeSubtaskType(HashMap<Integer, Subtask> subtasks) {
        subtasks.clear();
    }

    //Получение объекта по идентификатору
    public Task getTaskById(HashMap<Integer, Task> tasks, int id) {
        if (tasks.containsKey(id) || !tasks.isEmpty()) {
            return tasks.get(id);
        } else {
            System.out.println("Задание с указанным id не содержится в карте или карта пуста");
            return null;
        }
    }

    public Epic getEpicById(HashMap<Integer, Epic> epics, int id) {
        if (epics.containsKey(id) || !epics.isEmpty()) {
            return epics.get(id);
        } else {
            System.out.println("Задание с указанным id не содержится в карте или карта пуста");
            return null;
        }
    }

    public Subtask getSubtaskById(HashMap<Integer, Subtask> subtasks, int id) {
        if (subtasks.containsKey(id) || subtasks.isEmpty()) {
            return subtasks.get(id);
        } else {
            System.out.println("Задание с указанным id не содержится в карте или карта пуста");
            return null;
        }
    }

    //Обновление задач
    public void updateTask(Task task, Status status) {
        int key = task.getId();
        if (tasks.containsKey(key)) {
            task.setStatus(status);
            tasks.remove(key);
            tasks.put(key, task);
        } else {
            System.out.println("Такого задания в менеджере заданий НЕТ!");
        }
    }

    public void updateSubtask(Subtask subtask, Status status) {
        int key = subtask.getId(); // id переданный таски
        int epicId = subtask.getEpicId();
        ArrayList<Integer> idList;
        if (subtasks.containsKey(key)) {
            subtask.setStatus(status);
            subtasks.remove(key);
            subtasks.put(key, subtask);
            Epic epic = epics.get(epicId);
            idList = epic.getSubtaskIdList();
            epic.setStatus(setEpicStatusById(idList));
        } else {
            System.out.println("Такого задания в менеджере заданий НЕТ!");
        }
    }

    // Расчитывает статус Эпика
    public Status setEpicStatusById(ArrayList<Integer> idList) {
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

    //Получение списка всех подзадач определённого эпика
    public ArrayList<Subtask> getListSubtasksFromEpicId(HashMap<Integer, Subtask> subtasks,
                                                        HashMap<Integer, Epic> epics,
                                                        Integer epicId) {
        ArrayList<Integer> arrId;
        Epic epic = epics.get(epicId);
        arrId = epic.getSubtaskIdList();
        ArrayList<Subtask> st = new ArrayList<>();
        for (Integer integer : arrId) {
            st.add(subtasks.get(integer));
        }
        return st;
    }

}
