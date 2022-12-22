import model.EpicTask;
import model.SubTask;
import model.Task;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {

        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault();

        Task taskOne = new Task("Название", "Описание");
        Task taskTwo = new Task("Еще одна название", "А тут еще одно описание");

        EpicTask epicTaskOne = new EpicTask("Эпичное название", "Такое же описание");
        EpicTask epicTaskTwo = new EpicTask("Еще один Эпик", "УРААА");

        SubTask subTaskOne = new SubTask("Обычное подзадача", "C обычным описанием");
        SubTask subTaskTwo = new SubTask("Еще более простая подзадача", "с еще более обычным описанием");
        SubTask subTaskThree = new SubTask("Самая простая подзадача", "с самым обычным описанием");

        taskManager.createNewTask(taskOne);
        taskManager.createNewTask(taskTwo);
        taskManager.createNewEpicTask(epicTaskOne);
        taskManager.createNewSubTask(subTaskOne, epicTaskOne);
        taskManager.createNewSubTask(subTaskTwo, epicTaskOne);
        taskManager.createNewSubTask(subTaskThree, epicTaskOne);
        taskManager.createNewEpicTask(epicTaskTwo);

        System.out.println("Сделам несколько запросов, удалим часть тасков, и выведем историю");
        taskManager.getTask(1);
        taskManager.getEpicTask(3);
        taskManager.getTask(1);
        taskManager.getSubTask(4);
        taskManager.getEpicTask(7);
        taskManager.getTask(2);
        taskManager.getSubTask(6);
        taskManager.getSubTask(4);
        taskManager.getSubTask(5);
        System.out.println(historyManager.getHistory());

        System.out.println("Удалим обычную задачи и проверим историю");
        taskManager.removeTask(2);
        System.out.println(historyManager.getHistory());

        System.out.println("Удалим эпик и проверим историю");
        taskManager.removeEpicTask(3);
        System.out.println(historyManager.getHistory());
    }
}
