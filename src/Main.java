import Server.HttpTaskServer;
import Server.KVServer;
import Test.HttpTaskServerTest;
import model.EpicTask;
import model.SubTask;
import model.Task;

import service.FileBackedTasksManager;
import service.HttpTaskManager;
import service.Managers;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Random;


public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskManager taskManager = (HttpTaskManager) Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer((taskManager));
        httpTaskServer.start();
    }
}
