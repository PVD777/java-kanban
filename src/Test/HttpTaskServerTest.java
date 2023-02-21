package Test;

import Server.HttpTaskServer;
import Server.KVServer;
import com.google.gson.Gson;
import model.EpicTask;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HttpTaskManager;
import service.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


public class HttpTaskServerTest {
    KVServer kvServer;
    HttpTaskManager taskManager;
    HttpTaskServer httpTaskServer;

    final private Gson gson = Managers.getGson();

    private Task task1;
    private Task task2;
    private EpicTask epic1;
    private EpicTask epic2;
    private SubTask sub1;
    private SubTask sub2;


    public void tasksCreating() {
        task1 = new Task("taskName1", "taskDesc1", "01.01.2023|10:00", 60L);
        task2 = new Task("taskName2", "taskDesc2", "01.01.2023|10:00", 60L);
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        epic1 = new EpicTask("epicName1", "epicDesc1");
        epic2 = new EpicTask("epicName2", "epicDesc2");
        taskManager.createNewEpicTask(epic1);
        taskManager.createNewEpicTask(epic2);
        sub1 = new SubTask("subName1", "subDesc1", "02.02.2023|12:00", 10L, epic1);
        sub2 = new SubTask("subName2", "subDesc2", "03.03.2023|13:00", 30L, epic1);
        taskManager.createNewSubTask(sub1);
        taskManager.createNewSubTask(sub2);


    }

    @Test
    public void shouldAddTasks() throws IOException, InterruptedException {
        Assertions.assertTrue(taskManager.findAllTasks().isEmpty());
        String serializedTask = "{\"id\":1,\"name\":\"Название\",\"description\":\"Описание\",\"status\":\"NEW\",\"type\":\"TASK\",\"duration\":null,\"startTime\":null}";
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(serializedTask))
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("Задача создана", response.body());
        Assertions.assertEquals(gson.toJson(taskManager.getTasks().get(1)), serializedTask);
    }

    @Test
    public void shouldGetTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(gson.toJson(taskManager.findAllTasks()), response.body());
    }


    @Test
    public void shouldGetTaskById() throws IOException, InterruptedException {
        tasksCreating();
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(gson.toJson(taskManager.getTasks().get(1)), response.body());
        Assertions.assertEquals(taskManager.getTasks().get(1), gson.fromJson(response.body().toString(), Task.class));
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldGet405ByWrongTaskId() throws IOException, InterruptedException {
        tasksCreating();
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=999");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(405, response.statusCode());
    }

    @Test
    public void shouldDeleteTask() throws IOException, InterruptedException {
        tasksCreating();
        HttpClient client = HttpClient.newHttpClient();
        URI deleteOneTask = URI.create("http://localhost:8080/tasks/task/?id=1");
        URI deleteAllTasks = URI.create("http://localhost:8080/tasks/task/");

        HttpRequest requestForOne = HttpRequest.newBuilder()
                .uri(deleteOneTask)
                .DELETE()
                .build();

        HttpRequest requestForAll = HttpRequest.newBuilder()
                .uri(deleteAllTasks)
                .DELETE()
                .build();
        Assertions.assertEquals(taskManager.findAllTasks().size(), 2);

        client.send(requestForOne, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(taskManager.findAllTasks().size(), 1);

        client.send(requestForAll, HttpResponse.BodyHandlers.ofString());
        Assertions.assertTrue(taskManager.findAllTasks().isEmpty());
    }


    @Test
    public void shouldAddEpic() throws IOException, InterruptedException {
        epic1 = new EpicTask("epicName1", "epicDesc1");
        epic2 = new EpicTask("epicName2", "epicDesc2");
        Assertions.assertTrue(taskManager.findAllEpicTasks().isEmpty());
        taskManager.createNewEpicTask(epic1);
        taskManager.createNewEpicTask(epic2);
        String serializedTask = "{\"subTasksID\":null,\"endTime\":null,\"id\":3,\"name\":\"Название\",\"description\":\"Описание\",\"status\":\"NEW\",\"type\":\"EPIC\",\"duration\":null,\"startTime\":null}";
        //EpicTask epic = gson.fromJson(serializedTask, EpicTask.class);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(serializedTask))
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("Задача создана", response.body());
        Assertions.assertEquals(gson.toJson(taskManager.getEpicTasks().get(3)), serializedTask);
    }

    @Test
    public void shouldGetEpics() throws IOException, InterruptedException {
        tasksCreating();
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(gson.toJson(taskManager.findAllEpicTasks()), response.body());
    }

    @Test
    public void shouldGetEpicById() throws IOException, InterruptedException {
        tasksCreating();
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(gson.toJson(taskManager.getEpicTasks().get(3)), response.body());
        Assertions.assertEquals(taskManager.getEpicTasks().get(3), gson.fromJson(response.body().toString(), EpicTask.class));
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldGet405ByWrongEpicId() throws IOException, InterruptedException {
        tasksCreating();
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/Epic/?id=999");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(405, response.statusCode());
    }

    @Test
    public void shouldDeleteEpic() throws IOException, InterruptedException {
        tasksCreating();
        HttpClient client = HttpClient.newHttpClient();
        URI deleteOneTask = URI.create("http://localhost:8080/tasks/epic/?id=3");
        URI deleteAllTasks = URI.create("http://localhost:8080/tasks/epic/");

        HttpRequest requestForOne = HttpRequest.newBuilder()
                .uri(deleteOneTask)
                .DELETE()
                .build();

        HttpRequest requestForAll = HttpRequest.newBuilder()
                .uri(deleteAllTasks)
                .DELETE()
                .build();
        Assertions.assertEquals(taskManager.findAllEpicTasks().size(), 2);

        client.send(requestForOne, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(taskManager.findAllEpicTasks().size(), 1);

        client.send(requestForAll, HttpResponse.BodyHandlers.ofString());
        Assertions.assertTrue(taskManager.findAllEpicTasks().isEmpty());
    }

    @Test
    public void shouldAddSub() throws IOException, InterruptedException {
        Assertions.assertTrue(taskManager.findAllSubTasks().isEmpty());
        epic1 = new EpicTask("epicName1", "epicDesc1");
        taskManager.createNewEpicTask(epic1);
        String serializedTask = "{\"epicId\":1,\"id\":2,\"name\":\"Название\",\"description\":\"Описание\",\"status\":\"NEW\",\"type\":\"SUBTASK\",\"duration\":null,\"startTime\":null}";
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(serializedTask))
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("Задача создана", response.body());
        Assertions.assertEquals(gson.toJson(taskManager.getSubTasks().get(2)), serializedTask);
    }

    @Test
    public void shouldGetSubs() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(gson.toJson(taskManager.findAllSubTasks()), response.body());
    }


    @Test
    public void shouldGetSubById() throws IOException, InterruptedException {
        tasksCreating();
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=5");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(gson.toJson(taskManager.getSubTasks().get(5)), response.body());
        Assertions.assertEquals(taskManager.getSubTasks().get(5), gson.fromJson(response.body().toString(), SubTask.class));
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldGet405ByWrongSubId() throws IOException, InterruptedException {
        tasksCreating();
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/sub/?id=999");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(405, response.statusCode());
    }

    @Test
    public void shouldDeleteSub() throws IOException, InterruptedException {
        tasksCreating();
        HttpClient client = HttpClient.newHttpClient();
        URI deleteOneTask = URI.create("http://localhost:8080/tasks/subtask/?id=5");
        URI deleteAllTasks = URI.create("http://localhost:8080/tasks/subtask/");

        HttpRequest requestForOne = HttpRequest.newBuilder()
                .uri(deleteOneTask)
                .DELETE()
                .build();

        HttpRequest requestForAll = HttpRequest.newBuilder()
                .uri(deleteAllTasks)
                .DELETE()
                .build();
        Assertions.assertEquals(taskManager.findAllSubTasks().size(), 2);

        client.send(requestForOne, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(taskManager.findAllSubTasks().size(), 1);

        client.send(requestForAll, HttpResponse.BodyHandlers.ofString());
        Assertions.assertTrue(taskManager.findAllSubTasks().isEmpty());
    }

    @Test
    public void shouldGetHistory() throws IOException, InterruptedException {
        tasksCreating();
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(response.body(), taskManager.getHistory().toString());

        URI taskUri = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest requestTask = HttpRequest.newBuilder()
                .uri(taskUri)
                .GET()
                .build();
        URI epicUri = URI.create("http://localhost:8080/tasks/epic/?id=3");
        HttpRequest requestEpic = HttpRequest.newBuilder()
                .uri(epicUri)
                .GET()
                .build();
        URI subUri = URI.create("http://localhost:8080/tasks/subtask/?id=5");
        HttpRequest requestSub = HttpRequest.newBuilder()
                .uri(subUri)
                .GET()
                .build();
        client.send(requestTask, HttpResponse.BodyHandlers.ofString());
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        client.send(requestSub, HttpResponse.BodyHandlers.ofString());
        client.send(requestSub, HttpResponse.BodyHandlers.ofString());
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(taskManager.getHistory(), List.of(task1, epic1, sub1));
    }

    @Test
    public void shouldGetAllTasks() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(response.body(), "history is empty");
        tasksCreating();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(response.body(), gson.toJson(List.of(task1, task2, epic1, epic2, sub1, sub2)));
    }

    @BeforeEach
    private void setManager() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer((taskManager));
        httpTaskServer.start();
    }

    @AfterEach
    private void serverStop() {
        kvServer.stop();
        httpTaskServer.stop();
    }


}