package memory;

import file.ManagerSaveException;
import file.NotFoundException;
import file.Status;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void removeTaskType() {
        super.removeTaskType();
        save();
    }

    @Override
    public void removeEpicType() {
        super.removeEpicType();
        save();
    }

    @Override
    public void removeSubtaskType() {
        super.removeSubtaskType();
        save();
    }

    @Override
    public void updateTask(Task task) throws NotFoundException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws NotFoundException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeTaskById(int id) throws NotFoundException {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) throws NotFoundException {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) throws NotFoundException {
        super.removeSubtaskById(id);
        save();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (br.ready()) {
                String line = br.readLine();
                if (fromString(line).getClass().getName().equals("model.Task")) {
                    fileBackedTaskManager.getTasks().put(fromString(line).getId(), fromString(line));
                }
                if (fromString(line).getClass().getName().equals("model.Epic")) {
                    fileBackedTaskManager.getEpics().put(fromString(line).getId(), (Epic) fromString(line));
                }
                if (fromString(line).getClass().getName().equals("model.Subtask")) {
                    fileBackedTaskManager.getSubtasks().put(fromString(line).getId(), (Subtask) fromString(line));
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка чтения из файла");
        }
        return fileBackedTaskManager;
    }

    private void save() {
        List<String> allTasks = new ArrayList<>();
        for (Task task : getTasks().values()) {
            allTasks.add(task.toString());
        }
        for (Epic epic : getEpics().values()) {
            allTasks.add(epic.toString());
        }
        for (Subtask subtask : getSubtasks().values()) {
            allTasks.add(subtask.toString());
        }
        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8)) {
            for (String allTask : allTasks) {
                fw.write(allTask);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Неудачная попытка записи в файл");
        }
    }

    private static Task fromString(String value) {
        String[] fields = value.split(",");
        Task loadTask;
        if (fields[1].equals("TASK")) {
            loadTask = new Task(fields[2], fields[4], Integer.parseInt(fields[6]));
        } else if (fields[1].equals("EPIC")) {
            loadTask = new Epic(fields[2], fields[4]);
            loadTask.setDuration(Duration.ofMinutes(Integer.parseInt(fields[6])));
        } else {
            loadTask = new Subtask(fields[2], fields[4], Integer.parseInt(fields[8]), Integer.parseInt(fields[6]));
        }
        loadTask.setId(Integer.parseInt(fields[0]));
        loadTask.setStatus(Status.valueOf(fields[3]));
        loadTask.setStartTime(LocalDateTime.parse(fields[5], Task.formatter));
        loadTask.setEndTime(LocalDateTime.parse(fields[7], Task.formatter));
        return loadTask;
    }
}
