package memory;

import file.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest {
    File file;

    @BeforeEach
    public void beforeEach() {
        try {
            file = File.createTempFile("save", ".CSV");
            manager = FileBackedTaskManager.loadFromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.beforeEach();
    }

    @Test
    public void giveTasksOfDifferentTypesAndFindThemById() throws NotFoundException {
        super.giveTasksOfDifferentTypesAndFindThemById();
    }

    @Test
    public void checkThatTasksWithTheGivenIdAndTheGeneratedDoNotConflict() {
        super.checkThatTasksWithTheGivenIdAndTheGeneratedDoNotConflict();
    }

    @Test
    public void checkingThatTheTaskHasNotChangedInAllFieldsAfterAddingToTheManager() {
        super.checkingThatTheTaskHasNotChangedInAllFieldsAfterAddingToTheManager();
    }

    @Test
    public void tasksAddedToHistoryManagerRetainThePreviousVersionOfTheTask() throws NotFoundException {
        super.tasksAddedToHistoryManagerRetainThePreviousVersionOfTheTask();
    }

    @Test
    public void calculationOfBoundaryValuesAllSubtasksWithStatus() throws NotFoundException {
        super.calculationOfBoundaryValuesAllSubtasksWithStatus();
    }

    @Test
    public void checkingForIntersectionOfTimeIntervals() {
        super.checkingForIntersectionOfTimeIntervals();
    }

    @Test
    public void savingAndLoadingAnEmptyFile() {
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
    public void saveAndLoad() throws NotFoundException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        String actualTask = task1.toString();
        String actualEpic = epic.toString();
        String actualSubtask = subtask1.toString();
        FileBackedTaskManager fbNew = FileBackedTaskManager.loadFromFile(file);
        String expectedTask = fbNew.getTaskById(task1.getId()).toString();
        String expectedEpic = fbNew.getEpicById(epic.getId()).toString();
        String expectedSubtask = fbNew.getSubtaskById(subtask1.getId()).toString();
        assertEquals(expectedTask, actualTask, "Сохраненые и загруженные Taskи несовпадают");
        assertEquals(expectedEpic, actualEpic, "Сохраненые и загруженные Epicи несовпадают");
        assertEquals(expectedSubtask, actualSubtask, "Сохраненые и загруженные Subtaskи несовпадают");
    }

    @Test
    public void catchingExceptions() {
        file = new File("NotFound.CSV");
        assertThrows(IOException.class, () -> {
            BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
            while (br.ready()) {
                String line = br.readLine();
                System.out.println(line);
            }
        }, "Тест провален!");
    }

    @Test
    public void doesNotExceptions() {
        String line = "Ели мясо мужики, пивом запивали";
        assertDoesNotThrow(() -> {
            FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8);
            fw.write(line);
        }, "Тест провален!");
    }
}