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
    public void createNewTask(Task task) throws IOException {
            super.createNewTask(task);
            save();
    }

    @Override
    public void createNewEpicTask(EpicTask task) throws IOException {
            super.createNewEpicTask(task);
            save();
    }

    @Override
    public void createNewSubTask(SubTask task) throws IOException {
            super.createNewSubTask(task);
            save();
    }

    @Override
    public void clearTasks() throws IOException {
            super.clearTasks();
            save();
    }

    @Override
    public void clearEpicTasks() throws IOException {
            super.clearEpicTasks();
            save();
    }

    @Override
    public void clearSubTasks() throws IOException {
            super.clearSubTasks();
            save();
    }

    @Override
    public Task getTask(int uniqId) throws IOException {
            if (tasks.containsKey(uniqId)) {
                historyManager.add(tasks.get(uniqId));
                save();
                return tasks.get(uniqId);
            }
        return null;
    }

    @Override
    public EpicTask getEpicTask(int uniqId) throws IOException {
            if (epicTasks.containsKey(uniqId)) {
                historyManager.add(epicTasks.get(uniqId));
                save();
                return epicTasks.get(uniqId);
            }
        return null;
    }

    @Override
    public SubTask getSubTask(int uniqId) throws IOException {
            if (subTasks.containsKey(uniqId)) {
                historyManager.add(subTasks.get(uniqId));
                save();
                return subTasks.get(uniqId);
            }
        return null;
    }

    @Override
    public void updateTask(Task task) throws IOException {
            super.updateTask(task);
            save();
    }

    @Override
    public void updateEpicTask(EpicTask task) throws IOException {
            super.updateEpicTask(task);
            save();
    }

    @Override
    public void updateSubTask(SubTask task) throws IOException {
            super.updateSubTask(task);
            save();
    }

    @Override
    public void removeTask(int uniqId) throws IOException {
            super.removeTask(uniqId);
            save();
    }

    @Override
    public void removeEpicTask(int uniqId) throws IOException {
            super.removeEpicTask(uniqId);
            save();
    }

    @Override
    public void removeSubTask(int uniqId) throws IOException {
            super.removeSubTask(uniqId);
            save();
    }

    private void save() {
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
        } catch (NullPointerException e) {

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
        try {
            List<Integer> historyId = new ArrayList<>();
            String[] IdArray = value.split(", ");
            for (String id : IdArray) {
                historyId.add(Integer.parseInt(id));
            }
            return historyId;
        } catch (NullPointerException e) {

        }
        return null;
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

    public static int getLineCountByReader(String fileName) throws IOException {
        try (var lnr = new LineNumberReader(new BufferedReader(new FileReader(fileName)))) {
            while (lnr.readLine() != null) ;
            return lnr.getLineNumber();
        }
    }
}


