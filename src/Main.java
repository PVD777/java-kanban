import server.HttpTaskServer;
import server.KVServer;

import service.HttpTaskManager;
import service.Managers;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskManager taskManager = (HttpTaskManager) Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer((taskManager));
        httpTaskServer.start();
    }
}
