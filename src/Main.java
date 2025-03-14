public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Epic epic0 = new Epic("Name Test Epic_0", "Description Test Epic_0");
        Subtask subtask1 = new Subtask("Name Test Subtask_1", "Description Test Subtask_1", 0);
        Subtask subtask2 = new Subtask("Name Test Subtask_2", "Description Test Subtask_2", 0);
        Subtask subtask3 = new Subtask("Name Test Subtask_3", "Description Test Subtask_3", 0);
        Epic epic4 = new Epic("Name Test Epic_4", "Description Test Epic_4");

        inMemoryTaskManager.addEpic(epic0);
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);
        inMemoryTaskManager.addEpic(epic4);

        System.out.println("Запрос epic0 и вывод истории:");
        inMemoryTaskManager.getEpicById(epic0.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println("Запрос subtask2 и вывод истории:");
        inMemoryTaskManager.getSubtaskById(subtask2.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println("Запрос epic4 и вывод истории:");
        inMemoryTaskManager.getEpicById(epic4.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println("Запрос subtask3 и вывод истории:");
        inMemoryTaskManager.getSubtaskById(subtask3.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println("Запрос subtask1 и вывод истории:");
        inMemoryTaskManager.getSubtaskById(subtask1.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println("Запрос epic4 и вывод истории:");
        inMemoryTaskManager.getEpicById(epic4.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println("Запрос subtask2 и вывод истории:");
        inMemoryTaskManager.getSubtaskById(subtask2.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println("Запрос epic0 и вывод истории:");
        inMemoryTaskManager.getEpicById(epic0.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println("Удаление подзадачи subtask2 и вывод истории:");
        inMemoryTaskManager.removeSubtaskById(subtask2.getId());
        System.out.println(inMemoryTaskManager.getHistory());
    }
}

