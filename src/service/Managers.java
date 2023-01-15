package service;


import java.io.IOException;

public class Managers {


    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }


    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
    public static FileBackedTasksManager getFileBackedTasksManager(String path) throws IOException {
        return new FileBackedTasksManager(path);
    }

}
