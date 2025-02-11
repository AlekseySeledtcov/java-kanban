public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();

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
        taskManager.addEpicToHash(epic1);
        taskManager.addEpicToHash(epic2);
        taskManager.addSubtaskToHash(subtask2);
        taskManager.addSubtaskToHash(subtask1_1);
        taskManager.addSubtaskToHash(subtask1_2);

    }
}
