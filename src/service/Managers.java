package service;

import Server.LocalDateTimeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

public class Managers {


/*    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }*/

    public static HttpTaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager(URI.create("Http://localhost:8078"));
    }


    public static TaskManager getFileManager() throws IOException { return FileBackedTasksManager
            .loadFromFile(new File(System.getProperty("user.dir") + File.separator + "resources" +
                    File.separator + "Task List.csv"));}
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.setDateFormat("dd.MM.yyyy|HH:mm");
        gsonBuilder.serializeNulls();
        return gsonBuilder.create();
    }



}
