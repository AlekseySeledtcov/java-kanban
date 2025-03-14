import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Epic epic_0 = new Epic("Name Test Epic_0", "Description Test Epic_0");
        Subtask subtask_1 = new Subtask("Name Test Subtask_1", "Description Test Subtask_1", 0);
        Subtask subtask_2 = new Subtask("Name Test Subtask_2", "Description Test Subtask_2", 0);
        Subtask subtask_3 = new Subtask("Name Test Subtask_3", "Description Test Subtask_3", 0);
        Epic epic_4 = new Epic("Name Test Epic_4", "Description Test Epic_4");

        inMemoryTaskManager.addEpic(epic_0);
        inMemoryTaskManager.addSubtask(subtask_1);
        inMemoryTaskManager.addSubtask(subtask_2);
        inMemoryTaskManager.addSubtask(subtask_3);
        inMemoryTaskManager.addEpic(epic_4);

        System.out.println("Запрос epic_0 и вывод истории:");
        inMemoryTaskManager.getEpicById(epic_0.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println("Запрос subtask_2 и вывод истории:");
        inMemoryTaskManager.getSubtaskById(subtask_2.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println("Запрос epic_4 и вывод истории:");
        inMemoryTaskManager.getEpicById(epic_4.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println("Запрос subtask_3 и вывод истории:");
        inMemoryTaskManager.getSubtaskById(subtask_3.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println("Запрос subtask_1 и вывод истории:");
        inMemoryTaskManager.getSubtaskById(subtask_1.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println("Запрос epic_4 и вывод истории:");
        inMemoryTaskManager.getEpicById(epic_4.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println("Запрос subtask_2 и вывод истории:");
        inMemoryTaskManager.getSubtaskById(subtask_2.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println("Запрос epic_0 и вывод истории:");
        inMemoryTaskManager.getEpicById(epic_0.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println("Удаление подзадачи subtask_2 и вывод истории:");
        inMemoryTaskManager.removeSubtaskById(subtask_2.getId());
        System.out.println(inMemoryTaskManager.getHistory());
    }
}

