package service;

import Server.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.EpicTask;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Set;

public class HttpTaskManager extends FileBackedTasksManager {


    KVTaskClient kvTaskClient;
    Gson gson = Managers.getGson();

    public HttpTaskManager(URI uri) throws IOException, InterruptedException {

        kvTaskClient = new KVTaskClient(uri);
        load();
    }

    @Override
    public void save() {
        URI tasksUri = URI.create("http://localhost:8078/save/tasks?API_TOKEN=" + kvTaskClient.getAPI_TOKEN());
        URI epicsUri = URI.create("http://localhost:8078/save/epics?API_TOKEN=" + kvTaskClient.getAPI_TOKEN());
        URI subsUri = URI.create("http://localhost:8078/save/subtasks?API_TOKEN=" + kvTaskClient.getAPI_TOKEN());

        List<Task> allTasks = findAllTasks();
        List<EpicTask> allEpics = findAllEpicTasks();
        List<SubTask> allSubs = findAllSubTasks();
        List<Task> history = getHistory();

        String serializedTask = gson.toJson(allTasks);
        String serializedEpic = gson.toJson(allEpics);
        String serializedSub = gson.toJson(allSubs);
        String serializedHistory = gson.toJson(history);

        kvTaskClient.put("tasks", serializedTask);
        kvTaskClient.put("epic", serializedEpic);
        kvTaskClient.put("sub", serializedSub);
        kvTaskClient.put("history", serializedHistory);
    }

    public void load() {

        Gson gson = Managers.getGson();
        String taskJson = kvTaskClient.load("task");
        String epicJson = kvTaskClient.load("epic");
        String subJson = kvTaskClient.load("sub");

        Set<Task> tasksFromJson = gson.fromJson(taskJson, new TypeToken<Set<Task>>() {
        }.getType());
        if (tasksFromJson != null) {
            for (Task task : tasksFromJson) {
                this.tasks.put(task.getId(), task);
            }
        }
        Set<EpicTask> epicFromJson = gson.fromJson(epicJson, new TypeToken<Set<EpicTask>>() {
        }.getType());
        if (epicFromJson != null) {
            for (EpicTask task : epicFromJson) {
                this.epicTasks.put(task.getId(), task);
            }
        }

        Set<SubTask> subFromJson = gson.fromJson(subJson, new TypeToken<Set<SubTask>>() {
        }.getType());
        if (subFromJson != null) {
            for (SubTask task : subFromJson) {
                this.subTasks.put(task.getId(), task);
            }
        }
    }

}
