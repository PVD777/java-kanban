import model.EpicTask;
import model.SubTask;
import model.Task;

import service.FileBackedTasksManager;
import service.Managers;
import service.TaskManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {
        FileBackedTasksManager taskManager = FileBackedTasksManager.loadFromFile(new File(System.getProperty("user.dir") + File.separator + "resources"
                + File.separator + "Task List.csv"));

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

        taskManager.getTask(1);
        taskManager.getEpicTask(3);
        taskManager.getTask(1);
        taskManager.getSubTask(5);
        taskManager.getEpicTask(4);
        taskManager.getTask(2);
        taskManager.getSubTask(6);
        taskManager.getSubTask(5);
        taskManager.getSubTask(7);

        System.out.println("Задачи:");
        System.out.println(taskManager.findAllTasks() + " " + taskManager.findAllEpicTasks() + " " + taskManager.findAllSubTasks());
        System.out.println("История запросов");
        System.out.println(taskManager.getHistory());

        FileBackedTasksManager loadedTaskManager = FileBackedTasksManager.loadFromFile(new File(System.getProperty("user.dir") + File.separator + "resources"
                + File.separator + "Task List.csv"));
        System.out.println(loadedTaskManager.getHistory());




    }
}
