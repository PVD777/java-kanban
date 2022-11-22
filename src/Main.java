public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task taskOne = new Task("Название", "Описание");
        Task taskTwo = new Task("Еще одна название", "А тут еще одно описание");
        EpicTask epicTaskOne = new EpicTask("Эпичное название", "Такое же описание");
        SubTask subTaskOne = new SubTask("Обычное подзадача", "C обычным описанием");
        SubTask subTaskTwo = new SubTask("Еще более простая подзадача", "с еще более обычным описанием");
        EpicTask epicTaskTwo = new EpicTask("Еще один Эпик", "УРААА");
        SubTask subTaskThree = new SubTask("Хотел скреативить здесь", "Не получилось");
        System.out.println("Создаю простые задачи");
        taskManager.createNewTask(taskOne);
        taskManager.createNewTask(taskTwo);
        System.out.println(taskManager.findAllTasks());
        System.out.println("Удалаю простую задачу");
        taskManager.removeTask(2);
        System.out.println(taskManager.findAllTasks());
        System.out.println("Обновляю простую задачу");
        taskManager.updateTask(1, "Новое Название!", "Старое описание", taskManager.taskStatus[2]);
        System.out.println(taskManager.findAllTasks());
        System.out.println("Создачи эпики с подзадачами");
        taskManager.createNewEpicTask(epicTaskOne);
        taskManager.createNewSubTask(subTaskOne, epicTaskOne);
        taskManager.createNewSubTask(subTaskTwo, epicTaskOne);
        taskManager.createNewEpicTask(epicTaskTwo);
        taskManager.createNewSubTask(subTaskThree, epicTaskTwo);
        System.out.println(taskManager.findAllEpicTasks());
        System.out.println("Удалаю эпик");
        taskManager.removeEpicTask(6);
        System.out.println(taskManager.findAllEpicTasks());
        System.out.println("Обновляю подзадачу");
        taskManager.updateSubTask(4, "Задача посложнее", "описание посложнее", taskManager.taskStatus[2]);
        System.out.println(taskManager.findAllEpicTasks());
        System.out.println("Обновляю другую подзадачу того же эпика");
        taskManager.updateSubTask(5, "как сложно!", "Придумать описанаие!", taskManager.taskStatus[2]);
        System.out.println(taskManager.findAllEpicTasks());
    }
}
