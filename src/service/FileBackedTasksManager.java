package service;

import model.EpicTask;
import model.SubTask;
import model.Task;


import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {


    private String path;
    private final static String TABLE_HEADER = "id,type,name,status,description,epic";

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

    private void save()  {
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
            default:
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
            String[] IdArray = value.split(", ");
            for (String id : IdArray) {
                historyId.add(Integer.parseInt(id));
            }
            return historyId;
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
        int lineCount = getLineCountByReader(file.getPath());
        FileBackedTasksManager taskManager = new FileBackedTasksManager(file.getPath());
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine();
        for (int i = 2; i < lineCount - 1; i++) {
            taskManager.fromString(br.readLine());
        }
        br.readLine();
        taskManager.setHistory(historyFromString(br.readLine()));
        br.close();
        return taskManager;
    }

    private static int getLineCountByReader(String fileName) throws IOException {
        try (var lnr = new LineNumberReader(new BufferedReader(new FileReader(fileName)))) {
            while (lnr.readLine() != null) ;
            return lnr.getLineNumber();
        }
    }
}


