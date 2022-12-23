import model.EpicTask;
import model.SubTask;
import model.Task;

import service.Managers;
import service.TaskManager;


public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task taskOne = new Task("Название", "Описание");
        Task taskTwo = new Task("Еще одна название", "А тут еще одно описание");
        taskManager.createNewTask(taskOne);
        taskManager.createNewTask(taskTwo);
        EpicTask epicTaskOne = new EpicTask("Эпичное название", "Такое же описание");
        EpicTask epicTaskTwo = new EpicTask("Еще один Эпик", "УРААА");
        taskManager.createNewEpicTask(epicTaskOne);
        taskManager.createNewEpicTask(epicTaskTwo);
        SubTask subTaskOne = new SubTask("Обычное подзадача", "C обычным описанием", epicTaskOne);
        SubTask subTaskTwo = new SubTask("Еще более простая подзадача", "с еще более обычным описанием", epicTaskOne);
        SubTask subTaskThree = new SubTask("Самая простая подзадача", "с самым обычным описанием", epicTaskOne);
        taskManager.createNewSubTask(subTaskOne);
        taskManager.createNewSubTask(subTaskTwo);
        taskManager.createNewSubTask(subTaskThree);

        System.out.println("Сделам несколько запросов, удалим часть тасков, и выведем историю");
        taskManager.getTask(1);
        taskManager.getEpicTask(3);
        taskManager.getTask(1);
        taskManager.getSubTask(5);
        taskManager.getEpicTask(4);
        taskManager.getTask(2);
        taskManager.getSubTask(6);
        taskManager.getSubTask(5);
        taskManager.getSubTask(7);
        System.out.println(taskManager.getHistory());

        System.out.println("Удалим обычную задачи и проверим историю");
        taskManager.removeTask(2);
        System.out.println(taskManager.getHistory());

        System.out.println("Удалим эпик и проверим историю");
        taskManager.removeEpicTask(3);
        System.out.println(taskManager.getHistory());
        taskManager.clearEpicTasks();
        System.out.println("Удалим ВСЕ эпики и проверим историю");
        System.out.println(taskManager.getHistory());
    }
}
