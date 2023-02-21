package service;

import model.EpicTask;
import model.SubTask;
import model.Task;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private String path;
    private final static String TABLE_HEADER = "id,type,name,status,description,startTime,duration,epic";

    public FileBackedTasksManager(){}

    private FileBackedTasksManager(String path) {
        this.path = path;
    }

    @Override
    public void createNewTask(Task task)  {
            super.createNewTask(task);
            save();
    }

    @Override
    public void createNewEpicTask(EpicTask task) {
            super.createNewEpicTask(task);
            save();
    }

    @Override
    public void createNewSubTask(SubTask task) {
            super.createNewSubTask(task);
            save();
    }

    @Override
    public void clearTasks() {
            super.clearTasks();
            save();
    }

    @Override
    public void clearEpicTasks() {
            super.clearEpicTasks();
            save();
    }

    @Override
    public void clearSubTasks() {
            super.clearSubTasks();
            save();
    }

    @Override
    public Task getTask(int uniqId) {
            if (tasks.containsKey(uniqId)) {
                historyManager.add(tasks.get(uniqId));
                save();
                return tasks.get(uniqId);
            }
        return null;
    }

    @Override
    public EpicTask getEpicTask(int uniqId) {
            if (epicTasks.containsKey(uniqId)) {
                historyManager.add(epicTasks.get(uniqId));
                save();
                return epicTasks.get(uniqId);
            }
        return null;
    }

    @Override
    public SubTask getSubTask(int uniqId) {
            if (subTasks.containsKey(uniqId)) {
                historyManager.add(subTasks.get(uniqId));
                save();
                return subTasks.get(uniqId);
            }
        return null;
    }

    @Override
    public void updateTask(Task task) {
            super.updateTask(task);
            save();
    }

    @Override
    public void updateEpicTask(EpicTask task) {
            super.updateEpicTask(task);
            save();
    }

    @Override
    public void updateSubTask(SubTask task) {
            super.updateSubTask(task);
            save();
    }

    @Override
    public void removeTask(int uniqId) {
            super.removeTask(uniqId);
            save();
    }

    @Override
    public void removeEpicTask(int uniqId) {
            super.removeEpicTask(uniqId);
            save();
    }

    @Override
    public void removeSubTask(int uniqId) {
            super.removeSubTask(uniqId);
            save();
    }

    protected void save()  {
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
            throw new ManagerSaveException("Непредвиденная ошибка", e);
        }
    }

    private Task fromString(String value) {
        String[] taskInline = value.split("\\,", -1);
        int id = Integer.parseInt(taskInline[0]);
        String taskType = taskInline[1];
        String taskName = taskInline[2];
        String taskStatus = taskInline[3];
        String taskDescription = taskInline[4];
        String startTime = null;
        Long duration = null;
        if (!taskInline[5].equals("")) {
            startTime = taskInline[5];
        }
        if (!taskInline[6].equals("")) {
            duration = Long.parseLong(taskInline[6]);
        }

        switch (TaskType.valueOf(taskType)) {
            case TASK:
                tasks.put(id, new Task(taskName, taskDescription, startTime, duration));
                tasks.get(id).setStatus(TaskStatus.valueOf(taskInline[3]));
                tasks.get(id).setId(id);
                prioritizedTasks.add(tasks.get(id));
                return tasks.get(id);
            case EPIC:
                epicTasks.put(id, new EpicTask(taskName, taskDescription));
                epicTasks.get(id).setStatus(TaskStatus.valueOf(taskStatus));
                epicTasks.get(id).setId(id);
                prioritizedTasks.add(epicTasks.get(id));
                return epicTasks.get(id);
            case SUBTASK:
                subTasks.put(id, new SubTask(taskName, taskDescription, startTime, duration, epicTasks.get(Integer.parseInt(taskInline[7]))));
                subTasks.get(id).setStatus(TaskStatus.valueOf(taskStatus));
                subTasks.get(id).setId(id);
                epicTasks.get(Integer.parseInt(taskInline[7])).addSubTasksID(id);
                prioritizedTasks.add(subTasks.get(id));
                return subTasks.get(id);
            default:
                System.out.println("Произошла ошибка");
                throw new IllegalArgumentException();

        }
    }


    static String historyToString(HistoryManager manager) {
        List<String> idList = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            idList.add(String.valueOf(task.getId()));
        }
        return String.join(", ", idList);

    }

    static List<Integer> historyFromString(String value) {
            List<Integer> historyId = new ArrayList<>();
            if (!historyId.isEmpty()) {
                String[] IdArray = value.split(", ");
                for (String id : IdArray) {
                    historyId.add(Integer.parseInt(id));
                }
            }

            return historyId;
    }

    public void setHistory(List<Integer> historyList) {
            for (int id : historyList) {
                if (tasks.containsKey(id)) {
                    historyManager.add(tasks.get(id));
                }
                if (epicTasks.containsKey(id)) {
                    historyManager.add(epicTasks.get(id));
                }
                if (subTasks.containsKey(id)) {
                    historyManager.add(subTasks.get(id));
                }
            }
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
        int lineCount = getLineCountByReader(file.getPath());
        FileBackedTasksManager taskManager = new FileBackedTasksManager(file.getPath());
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine();
        try {
            for (int i = 1; i < lineCount - 1; i++) {
                taskManager.fromString(br.readLine());
            }
            br.readLine();
            taskManager.setHistory(historyFromString(br.readLine()));
            br.close();
        }
        catch ( NullPointerException e) {
            e.printStackTrace();
        }
        finally {
            taskManager.setCounterId(taskManager.findMaxId(taskManager));
            return taskManager;
        }

    }

    private static int getLineCountByReader(String fileName) throws IOException {
        try (var lnr = new LineNumberReader(new BufferedReader(new FileReader(fileName)))) {
            while (lnr.readLine() != null) ;
            return lnr.getLineNumber();
        }
    }


    private int findMaxId (TaskManager taskManager) {
        int maxID = 0;
        for (Task task : taskManager.findAllTasks()) {
            if (task.getId() > maxID) maxID = task.getId();
        }
        for (EpicTask task : taskManager.findAllEpicTasks()) {
            if (task.getId() > maxID) maxID = task.getId();
        }
        for (SubTask task : taskManager.findAllSubTasks()) {
            if (task.getId() > maxID) maxID = task.getId();
        }
        return maxID;
    }
}


