package service;

import model.EpicTask;
import model.SubTask;
import model.Task;


import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {


    String path;
    private final static String TABLE_HEADER = "id,type,name,status,description,epic";


    public FileBackedTasksManager() {

    }

    public FileBackedTasksManager(String path) throws IOException {
        this.path = path;
    }

    @Override
    public void createNewTask(Task task) {
        try {
            super.createNewTask(task);
            save();
        } catch (IOException e) {
            throw new ManagerSaveException("Непредвиденная ошибка");
        }
    }

    @Override
    public void createNewEpicTask(EpicTask task) {
        try {
            super.createNewEpicTask(task);
            save();
        } catch (IOException e) {
            throw new ManagerSaveException("Непредвиденная ошибка");
        }
    }

    @Override
    public void createNewSubTask(SubTask task) {
        try {
            super.createNewSubTask(task);
            save();
        } catch (IOException e) {
            throw new ManagerSaveException("Непредвиденная ошибка");
        }
    }

    @Override
    public void clearTasks() {
        try {
            super.clearTasks();
            save();
        } catch (IOException e) {
            throw new ManagerSaveException("Непредвиденная ошибка");
        }
    }

    @Override
    public void clearEpicTasks() {
        try {
            super.clearEpicTasks();
            save();
        } catch (IOException e) {
            throw new ManagerSaveException("Непредвиденная ошибка");
        }
    }

    @Override
    public void clearSubTasks() {
        try {
            super.clearSubTasks();
            save();
        } catch (IOException e) {
            throw new ManagerSaveException("Непредвиденная ошибка");
        }
    }

    @Override
    public Task getTask(int uniqId) {
        try {
            if (tasks.containsKey(uniqId)) {
                historyManager.add(tasks.get(uniqId));
                save();
                return tasks.get(uniqId);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Непредвиденная ошибка");
        }
        return null;
    }

    @Override
    public EpicTask getEpicTask(int uniqId) {
        try {
            if (epicTasks.containsKey(uniqId)) {
                historyManager.add(epicTasks.get(uniqId));
                save();
                return epicTasks.get(uniqId);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Непредвиденная ошибка");
        }
        return null;
    }

    @Override
    public SubTask getSubTask(int uniqId) {
        try {
            if (subTasks.containsKey(uniqId)) {
                historyManager.add(subTasks.get(uniqId));
                save();
                return subTasks.get(uniqId);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Непредвиденная ошибка");
        }
        return null;
    }

    @Override
    public void updateTask(Task task) {
        try {
            super.updateTask(task);
            save();
        } catch (IOException e) {
            throw new ManagerSaveException("Непредвиденная ошибка");
        }
    }

    @Override
    public void updateEpicTask(EpicTask task) {
        try {
            super.updateEpicTask(task);
            save();
        } catch (IOException e) {
            throw new ManagerSaveException("Непредвиденная ошибка");
        }
    }

    @Override
    public void updateSubTask(SubTask task) {
        try {
            super.updateSubTask(task);
            save();
        } catch (IOException e) {
            throw new ManagerSaveException("Непредвиденная ошибка");
        }
    }

    @Override
    public void removeTask(int uniqId) {
        try {
            super.removeTask(uniqId);
            save();
        } catch (IOException e) {
            throw new ManagerSaveException("Непредвиденная ошибка");
        }
    }

    @Override
    public void removeEpicTask(int uniqId) {
        try {
            super.removeEpicTask(uniqId);
            save();
        } catch (IOException e) {
            throw new ManagerSaveException("Непредвиденная ошибка");
        }
    }

    @Override
    public void removeSubTask(int uniqId) {
        try {
            super.removeSubTask(uniqId);
            save();
        } catch (IOException e) {
            throw new ManagerSaveException("Непредвиденная ошибка");
        }
    }

    void save() throws IOException {
        try {

            Writer fileWriter = new FileWriter(path, false);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.append(TABLE_HEADER);
            bw.newLine();

            for (Task task : tasks.values()) {
                bw.append(task.toString());
                bw.newLine();
            }
            for (EpicTask task : epicTasks.values()) {
                bw.append(task.toString());
                bw.newLine();
            }
            for (SubTask task : subTasks.values()) {
                bw.append(task.toString());
                bw.newLine();
            }
            bw.newLine();
            bw.append(historyToString(historyManager));

            bw.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Непредвиденная ошибка");
        }
    }

    Task fromString(String value) {
        String[] taskInline = value.split(",");

        int id = Integer.parseInt(taskInline[0]);
        String taskType = taskInline[1];
        String taskName = taskInline[2];
        String taskStatus = taskInline[3];
        String taskDescription = taskInline[4];

        switch (TaskType.valueOf(taskType)) {
            case TASK:
                tasks.put(id, new Task(taskName, taskDescription));
                tasks.get(id).setStatus(TaskStatus.valueOf(taskInline[3]));
                tasks.get(id).setId(id);
                return tasks.get(id);
            case EPIC:
                epicTasks.put(id, new EpicTask(taskName, taskDescription));
                epicTasks.get(id).setStatus(TaskStatus.valueOf(taskStatus));
                epicTasks.get(id).setId(id);
                return epicTasks.get(id);
            case SUBTASK:
                subTasks.put(id, new SubTask(taskName, taskDescription, epicTasks.get(Integer.parseInt(taskInline[5]))));
                subTasks.get(id).setStatus(TaskStatus.valueOf(taskStatus));
                subTasks.get(id).setId(id);
                epicTasks.get(Integer.parseInt(taskInline[5])).addSubTasksID(id);
                return subTasks.get(id);
        }
        return null;
    }


    static String historyToString(HistoryManager manager) {
        List<String> idList = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            idList.add(String.valueOf(task.getId()));
        }
        return String.join(", ", idList);

    }

    static List<Integer> historyFromString(String value) {
        try {
            List<Integer> historyId = new ArrayList<>();
            String[] IdArray = value.split(", ");
            for (String id : IdArray) {
                historyId.add(Integer.parseInt(id));
            }
            return historyId;
        } catch (NumberFormatException e) {
            throw new ManagerSaveException("Непредвиденная ошибка");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
        int lineCount = getLineCountByReader(file.getPath());
        FileBackedTasksManager taskManager = new FileBackedTasksManager();
        BufferedReader br = new BufferedReader(new FileReader(file));

        br.readLine();
        for (int i = 2; i < lineCount - 1; i++) {
            taskManager.fromString(br.readLine());
        }
        System.out.println("Выгружены следующие задачи:");
        System.out.println(taskManager.findAllTasks() + " " + taskManager.findAllEpicTasks() + " " + taskManager.findAllSubTasks());
        br.readLine();
        System.out.println("обнаружено в истории запросов");
        System.out.println(historyFromString(br.readLine()));
        br.close();
        return taskManager;
    }

    public static int getLineCountByReader(String fileName) throws IOException {
        try (var lnr = new LineNumberReader(new BufferedReader(new FileReader(fileName)))) {
            while (lnr.readLine() != null) ;
            return lnr.getLineNumber();
        }
    }
}


