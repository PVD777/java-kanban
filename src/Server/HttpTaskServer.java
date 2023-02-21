package Server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import model.EpicTask;
import model.SubTask;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;


public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    Gson gson;
    TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        gson = Managers.getGson();
        server.createContext("/tasks/task/", this::handleTask);
        server.createContext("/tasks/epic/", this::handleEpic);
        server.createContext("/tasks/subtask/", this::handleSub);
        server.createContext("/tasks/", this::handleAllTask);
        server.createContext("/tasks/history", this::handleHistory);
        server.createContext("/tasks/subtask/epic/", this::handleSubOfEpic);
    }

    private void handleSubOfEpic(HttpExchange exchange) {
        try {
            String requestPath = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();
            String requestMethod = exchange.getRequestMethod();
            if (!requestMethod.equals("GET")) {
                exchange.sendResponseHeaders(405, 0);
            }
            if (!requestPath.matches("^/tasks/subtask/epic/$")) {
                exchange.sendResponseHeaders(405, 0);
            }
            if (query != null && query.startsWith("id=")) {
                String taskIdString = query.split("id=")[1];
                int taskId = Integer.parseInt(taskIdString);
                System.out.println("Получение информации о подзадачах эпика №" + taskId);

                List<SubTask> subOfEpic = taskManager.findTasksOfEpic(taskManager.getEpicTasks().get(taskId));
                String response = gson.toJson(subOfEpic);
                sendText(exchange, response);
            }

            switch (requestMethod) {
                case "GET":
                    if (query != null && query.startsWith("id=")) {
                        String taskIdString = query.split("id=")[1];
                        int taskId = Integer.parseInt(taskIdString);
                        System.out.println("Получение информации о подзадаче №" + taskId);
                        String response = gson.toJson(taskManager.getSubTask(taskId));
                        sendText(exchange, response);
                    } else if (requestPath.matches("^/tasks/subtask/$")) {
                        System.out.println("Получение информации о всех подзадачах");
                        String response = gson.toJson(taskManager.findAllSubTasks());
                        sendText(exchange, response);
                    } else {
                        System.out.println("Некорректный метод GET");
                        exchange.sendResponseHeaders(405, 0);
                    }
                    break;
                case "POST":
                    String taskString = readText(exchange);
                    SubTask task = gson.fromJson(taskString, SubTask.class);
                    if (taskManager.getSubTasks().containsKey(task.getId())) {
                        taskManager.updateSubTask(task);
                        sendText(exchange, "Задача обновлена");
                    } else {
                        taskManager.createNewSubTask(task);
                        sendText(exchange, "Задача создана");
                    }
                    break;

                case "DELETE":
                    if (query != null && query.startsWith("id=")) {
                        String taskIdString = query.split("id=")[1];
                        int taskId = Integer.parseInt(taskIdString);
                        System.out.println("Удаление задачи № " + taskId);
                        taskManager.removeSubTask(taskId);
                        sendText(exchange, "Задача № " + taskId + " удалена");
                    } else if (requestPath.matches("^/tasks/subtask/$")) {
                        System.out.println("Удаление всех подзадач задач");
                        taskManager.clearSubTasks();
                        sendText(exchange, "Список подзадач очищен");
                    } else {
                        System.out.println("Некорректный метод delete");
                        exchange.sendResponseHeaders(405, 0);
                    }
                    break;

                default:
                    System.out.println("Некорректный метод");
                    exchange.sendResponseHeaders(405, 0);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleHistory(HttpExchange exchange) {
        try {
            String requestPath = exchange.getRequestURI().getPath();
            String requestMethod = exchange.getRequestMethod();
            if (!requestMethod.equals("GET")) {
                exchange.sendResponseHeaders(405, 0);
                return;
            }
            if (!requestPath.equals("/tasks/history")) {
                exchange.sendResponseHeaders(405, 0);
                return;
            }
            List<Task> history = new ArrayList<>();
            String response = gson.toJson(history);
            sendText(exchange, response);
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleAllTask(HttpExchange exchange) {
        try {
            String requestPath = exchange.getRequestURI().getPath();
            String requestMethod = exchange.getRequestMethod();
            if (!requestMethod.equals("GET")) {
                exchange.sendResponseHeaders(405, 0);
                return;
            }
            if (!requestPath.equals("/tasks/")) {
                exchange.sendResponseHeaders(405, 0);
                return;
            }
            List<Task> allTasks = new ArrayList<>();
            allTasks.addAll(taskManager.findAllTasks());
            allTasks.addAll(taskManager.findAllEpicTasks());
            allTasks.addAll(taskManager.findAllSubTasks());
            String response = "";
            if (allTasks.isEmpty()) {
                response = "history is empty";
            } else {
                response = gson.toJson(allTasks);
            }

            sendText(exchange, response);
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleSub(HttpExchange exchange) {
        try {
            String requestPath = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();
            String requestMethod = exchange.getRequestMethod();
            switch (requestMethod) {
                case "GET":
                    if (query != null && query.startsWith("id=")) {
                        String taskIdString = query.split("id=")[1];
                        int taskId = Integer.parseInt(taskIdString);
                        System.out.println("Получение информации о подзадаче №" + taskId);
                        if (taskManager.getSubTasks().containsKey(taskId)) {
                            String response = gson.toJson(taskManager.getSubTask(taskId));
                            sendText(exchange, response);
                        } else exchange.sendResponseHeaders(405, 0);
                    } else if (requestPath.matches("^/tasks/subtask/$")) {
                        System.out.println("Получение информации о всех подзадачах");
                        String response = gson.toJson(taskManager.findAllSubTasks());
                        sendText(exchange, response);
                    } else {
                        System.out.println("Некорректный метод GET");
                        exchange.sendResponseHeaders(405, 0);
                    }
                    break;
                case "POST":
                    String taskString = readText(exchange);
                    SubTask task = gson.fromJson(taskString, SubTask.class);
                    if (taskManager.getSubTasks().containsKey(task.getId())) {
                        taskManager.updateSubTask(task);
                        sendText(exchange, "Задача обновлена");
                    } else {
                        taskManager.createNewSubTask(task);
                        sendText(exchange, "Задача создана");
                    }
                    break;

                case "DELETE":
                    if (query != null && query.startsWith("id=")) {
                        String taskIdString = query.split("id=")[1];
                        int taskId = Integer.parseInt(taskIdString);
                        System.out.println("Удаление задачи № " + taskId);
                        taskManager.removeSubTask(taskId);
                        sendText(exchange, "Задача № " + taskId + " удалена");
                    } else if (requestPath.matches("^/tasks/subtask/$")) {
                        System.out.println("Удаление всех подзадач задач");
                        taskManager.clearSubTasks();
                        sendText(exchange, "Список подзадач очищен");
                    } else {
                        System.out.println("Некорректный метод delete");
                        exchange.sendResponseHeaders(405, 0);
                    }
                    break;

                default:
                    System.out.println("Некорректный метод");
                    exchange.sendResponseHeaders(405, 0);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            exchange.close();
        }
    }


    private void handleEpic(HttpExchange exchange) {
        try {
            String requestPath = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();
            String requestMethod = exchange.getRequestMethod();
            switch (requestMethod) {
                case "GET":
                    if (query != null && query.startsWith("id=")) {
                        String taskIdString = query.split("id=")[1];
                        int taskId = Integer.parseInt(taskIdString);
                        System.out.println("Получение информации о эпике №" + taskId);
                        if (taskManager.getEpicTasks().containsKey(taskId)) {
                            String response = gson.toJson(taskManager.getEpicTask(taskId));
                            sendText(exchange, response);
                        } else exchange.sendResponseHeaders(405, 0);
                    } else if (requestPath.matches("^/tasks/epic/$")) {
                        System.out.println("Получение информации о всех Эпиках");
                        String response = gson.toJson(taskManager.findAllEpicTasks());
                        sendText(exchange, response);
                    } else {
                        System.out.println("Некорректный метод GET");
                        exchange.sendResponseHeaders(405, 0);
                    }
                    break;
                case "POST":
                    String taskString = readText(exchange);
                    EpicTask task = gson.fromJson(taskString, EpicTask.class);
                    if (taskManager.getEpicTasks().containsKey(task.getId())) {
                        taskManager.updateEpicTask(task);
                        sendText(exchange, "Задача обновлена");
                    } else {
                        taskManager.createNewEpicTask(task);
                        sendText(exchange, "Задача создана");
                    }
                    break;

                case "DELETE":
                    if (query != null && query.startsWith("id=")) {
                        String taskIdString = query.split("id=")[1];
                        int taskId = Integer.parseInt(taskIdString);
                        System.out.println("Удаление задачи № " + taskId);
                        taskManager.removeEpicTask(taskId);
                        sendText(exchange, "Задача № " + taskId + " удалена");
                    } else if (requestPath.matches("^/tasks/epic/$")) {
                        System.out.println("Удаление всех эпиков");
                        taskManager.clearEpicTasks();
                        sendText(exchange, "Список Эпиков очищен");
                    } else {
                        System.out.println("Некорректный метод delete");
                        exchange.sendResponseHeaders(405, 0);
                    }
                    break;

                default:
                    System.out.println("Некорректный метод");
                    exchange.sendResponseHeaders(405, 0);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            exchange.close();
        }
    }


    private void handleTask(HttpExchange exchange) {
        try {
            String requestPath = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();
            String requestMethod = exchange.getRequestMethod();
            switch (requestMethod) {
                case "GET":
                    if (query != null && query.startsWith("id=")) {
                        String taskIdString = query.split("id=")[1];
                        int taskId = Integer.parseInt(taskIdString);
                        System.out.println("Получение информации о задаче №" + taskId);
                        if (taskManager.getTasks().containsKey(taskId)) {
                            String response = gson.toJson(taskManager.getTask(taskId));
                            sendText(exchange, response);
                        } else exchange.sendResponseHeaders(405, 0);
                    } else if (requestPath.matches("^/tasks/task/$")) {
                        System.out.println("Получение информации о всех простых задачах");
                        String response = gson.toJson(taskManager.findAllTasks());
                        sendText(exchange, response);
                    } else {
                        System.out.println("Некорректный метод GET");
                        exchange.sendResponseHeaders(405, 0);
                    }
                    break;
                case "POST":
                    String taskString = readText(exchange);
                    Task task = gson.fromJson(taskString, Task.class);
                    if (taskManager.getTasks().containsKey(task.getId())) {
                        taskManager.updateTask(task);
                        sendText(exchange, "Задача обновлена");
                    } else {
                        taskManager.createNewTask(task);
                        sendText(exchange, "Задача создана");
                    }
                    break;

                case "DELETE":
                    if (query != null && query.startsWith("id=")) {
                        String taskIdString = query.split("id=")[1];
                        int taskId = Integer.parseInt(taskIdString);
                        System.out.println("Удаление задачи № " + taskId);
                        taskManager.removeTask(taskId);
                        sendText(exchange, "Задача № " + taskId + " удалена");
                    } else if (requestPath.matches("^/tasks/task/$")) {
                        System.out.println("Удаление всех простых задач");
                        taskManager.clearTasks();
                        sendText(exchange, "Список простых задач очищен");
                    } else {
                        System.out.println("Некорректный метод delete");
                        exchange.sendResponseHeaders(405, 0);
                    }
                    break;

                default:
                    System.out.println("Некорректный метод");
                    exchange.sendResponseHeaders(405, 0);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    public void start() {
        System.out.println("HttpTaskServer запущен на http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer(Managers.getFileManager());
        server.start();
    }

}




