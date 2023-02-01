import model.EpicTask;
import model.SubTask;
import model.Task;

import service.FileBackedTasksManager;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Random;


public class Main {

    public static void main(String[] args) throws IOException {

        FileBackedTasksManager taskManager = FileBackedTasksManager.loadFromFile(new File(System.getProperty("user.dir") + File.separator + "resources"
                + File.separator + "Task List.csv"));

        Task taskOne = new Task("Название", "Описание", getRandomDateTimeOfPattern(), 5);
        Task taskTwo = new Task("Еще одна название", "А тут еще одно описание", getRandomDateTimeOfPattern(), 55);
        Task taskThree = new Task("Простая задача", "По конструктору без startTime");
        taskManager.createNewTask(taskOne);
        taskManager.createNewTask(taskTwo);
        taskManager.createNewTask(taskThree);
        EpicTask epicTaskOne = new EpicTask("Эпичное название", "Такое же описание", getRandomDateTimeOfPattern());
        EpicTask epicTaskTwo = new EpicTask("Еще один Эпик", "Но без startTime");
        EpicTask epicTaskThree = new EpicTask("Для эпичного эпика", "Эпичное время старта", "31.12.1999|23:59");
        taskManager.createNewEpicTask(epicTaskOne);
        taskManager.createNewEpicTask(epicTaskTwo);
        taskManager.createNewEpicTask(epicTaskThree);
        SubTask subTaskOne = new SubTask("Обычное подзадача", "C обычным описанием",
                getRandomDateTimeOfPattern(), 1,  epicTaskOne);
        SubTask subTaskTwo = new SubTask("Еще более простая подзадача", "Но без StartTime",
                epicTaskOne);
        SubTask subTaskThree = new SubTask("Самая простая подзадача", "с самым обычным описанием",
                getRandomDateTimeOfPattern(), 99, epicTaskOne);
        taskManager.createNewSubTask(subTaskOne);
        taskManager.createNewSubTask(subTaskTwo);
        taskManager.createNewSubTask(subTaskThree);
        System.out.println(taskManager.getPrioritizedTasks());
    }



    private static String getRandomDateTimeOfPattern() {
        Random random = new Random();
        LocalDateTime time = LocalDateTime.of(LocalDate.now().minus(Period.ofDays(random.nextInt(10))),
                LocalTime.of(random.nextInt(24), random.nextInt(60), random.nextInt(60)));
        return time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy|HH:mm"));
    }
}
