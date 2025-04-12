import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    File file;
    private Task task1;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void beforeEach() {
        task1 = new Task("Name Test Task", "Description Test Task");
        epic = new Epic("Name Test Epic", "Description Test Epic");
        subtask = new Subtask("Name Test Subtask", "Description Test Subtask", 1);
        try {
            file = File.createTempFile("save", ".CSV");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void savingAndLoadingAnEmptyFile() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        fileBackedTaskManager.addTask(task1);
        fileBackedTaskManager.removeTaskType();
        int sizeBeforeSaving = fileBackedTaskManager.getTasks().size();
        FileBackedTaskManager fbNew = FileBackedTaskManager.loadFromFile(file);
        int sizeAfterSaving = fbNew.getTasks().size();
        assertEquals(sizeBeforeSaving, sizeAfterSaving, "Размер мапы до сохранения пустого файла " +
                " и после загрузки - отличаются");
    }

    @Test
    void saveAndLoad() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        fileBackedTaskManager.addTask(task1);
        fileBackedTaskManager.addEpic(epic);
        fileBackedTaskManager.addSubtask(subtask);
        String actualTask = task1.toString();
        String actualEpic = epic.toString();
        String actualSubtask = subtask.toString();
        FileBackedTaskManager fbNew = FileBackedTaskManager.loadFromFile(file);
        String expectedTask = fbNew.getTaskById(task1.getId()).toString();
        String expectedEpic = fbNew.getEpicById(epic.getId()).toString();
        String expectedSubtask = fbNew.getSubtaskById(subtask.getId()).toString();
        assertEquals(expectedTask, actualTask, "Сохраненые и загруженные Taskи несовпадают");
        assertEquals(expectedEpic, actualEpic, "Сохраненые и загруженные Epicи несовпадают");
        assertEquals(expectedSubtask, actualSubtask, "Сохраненые и загруженные Subtaskи несовпадают");
    }
}