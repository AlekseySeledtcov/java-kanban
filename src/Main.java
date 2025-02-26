import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task1 = new Task("Имя Task 1", "Описание1");
        Task task2 = new Task("Имя Task 2", "Описание2");
        Task task3 = new Task("Имя Task 3", "Описание3");
        Epic epic1 = new Epic("Имя epic 1", "Описание эпика 1");
        Epic epic2 = new Epic("Имя epic 2", "Описание эпика 2");
        Subtask subtask1_1 = new Subtask("Сабтаск1_1", "Описание сабтаска 1_1", 4);
        Subtask subtask1_2 = new Subtask("Сабтаск1_2", "Описание сабтаска 1_2", 4);
        Subtask subtask2 = new Subtask("Сабтаск2", "Описание сабтаска 2", 5);

        taskManager.addTaskToHash(task1);
        taskManager.addTaskToHash(task2);
        taskManager.addTaskToHash(task3);
//        taskManager.addEpicToHash(epic1);
//        taskManager.addEpicToHash(epic2);
//        taskManager.addSubtaskToHash(subtask1_1);
//        taskManager.addSubtaskToHash(subtask1_2);
//        taskManager.addSubtaskToHash(subtask2);

        printAllTasks(taskManager);
    }
    private static void printAllTasks(InMemoryTaskManager taskManager) {
        System.out.println("Задачи:");
        for (Task task : taskManager.getListFromHashTask()) {
            taskManager.getTaskById(task.getId());
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : taskManager.getListFromHashEpic()) {
            taskManager.getEpicById(epic.getId());
            System.out.println(epic);
            for (Task task : taskManager.getEpicSubtask(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask :taskManager.getListFromHashSubtask()) {
            taskManager.getSubtaskById(subtask.getId());
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}

