import model.EpicTask;
import model.SubTask;
import model.Task;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;
import service.TaskStatus;

public class Main {
    public static void main(String[] args) {

        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault();

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
        taskOne.setName("Новое название");
        taskOne.setDescription("Новое описание");
        taskOne.setStatus(TaskStatus.DONE);
        taskManager.updateTask(taskOne);
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
        subTaskOne.setName("Задача посложнее");
        subTaskOne.setDescription("Описание посложнее");
        subTaskOne.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTaskOne);
        System.out.println(taskManager.findAllEpicTasks());

        System.out.println("Обновляю другую подзадачу того же эпика");
        subTaskTwo.setName("Очень сложно");
        subTaskTwo.setDescription("придумывать описания");
        subTaskTwo.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTaskTwo);
        System.out.println(taskManager.findAllEpicTasks());

        System.out.println("Сделам несколько запросов, и выведем историю");
        taskManager.getTask(1);
        taskManager.getEpicTask(3);
        taskManager.getSubTask(4);
        taskManager.getTask(1);
        taskManager.getEpicTask(3);
        taskManager.getSubTask(4);
        taskManager.getTask(1);
        taskManager.getEpicTask(3);
        taskManager.getSubTask(4);
        taskManager.getTask(1);
        taskManager.getEpicTask(3);
        taskManager.getSubTask(4);
        System.out.println(historyManager.getHistory());
    }
}
