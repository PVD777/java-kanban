package service;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private static int counterId = 0; // счетчик для номеров задач
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    // Подзадачи эпиков хранятся в полях самого эпика
    private final HashMap<Integer, EpicTask> epicTasks = new HashMap<>();

    @Override
    public ArrayList<Task> findAllTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();
        for (Integer id : tasks.keySet()) {
            tasksList.add(tasks.get(id));
        }
        return tasksList;
    }

    @Override
    public ArrayList<EpicTask> findAllEpicTasks() {
        ArrayList<EpicTask> epicTasksList = new ArrayList<>();
        for (Integer id : epicTasks.keySet()) {
            epicTasksList.add(epicTasks.get(id));
        }
        return epicTasksList;
    }

    @Override
    public ArrayList<SubTask> findAllSubTasks() {
        ArrayList<SubTask> subTasksList = new ArrayList<>();
        for (EpicTask epicTask : epicTasks.values()) {
            for (Integer id : epicTask.getSubTasks().keySet()) {
                subTasksList.add(epicTask.getSubTasks().get(id));
            }
        }
        return subTasksList;
    }

    @Override
    public ArrayList<SubTask> findTasksOfEpic(EpicTask epic) {
        ArrayList<SubTask> subTasksList = new ArrayList<>();
        for (Integer id : epic.getSubTasks().keySet()) {
            subTasksList.add(epic.getSubTasks().get(id));
        }
        return subTasksList;
    }

    @Override
    public void clearTask() {
        tasks.clear();
    }

    @Override
    public void clearEpicTask() {
        epicTasks.clear();
    }

    @Override
    public void clearSubTask() {
        for (EpicTask task : epicTasks.values()) {
            task.getSubTasks().clear();
        }
    }

    @Override
    public Task getTask(int uniqId) {
        if (tasks.containsKey(uniqId)) {
            historyManager.add(tasks.get(uniqId));
            return tasks.get(uniqId);
        }
        return null;
    }

    @Override
    public EpicTask getEpicTask(int uniqId) {
        if (epicTasks.containsKey(uniqId)) {
            historyManager.add(epicTasks.get(uniqId));
            return epicTasks.get(uniqId);
        }
        return null;
    }

    @Override
    public SubTask getSubTask(int uniqId) {
        for (EpicTask task : epicTasks.values()) {
            if (task.getSubTasks().containsKey(uniqId)) {
                historyManager.add(task.getSubTasks().get(uniqId));
                return task.getSubTasks().get(uniqId);
            }
        }
        return null;
    }

    @Override
    public void createNewTask(Task task) {
        tasks.put(++counterId, task);
        task.setStatus(TaskStatus.NEW);
        task.setUniqId(counterId);
    }

    @Override
    public void createNewEpicTask(EpicTask task) {
        epicTasks.put(++counterId, task);
        task.setStatus(TaskStatus.NEW);
        task.setUniqId(counterId);
    }

    public void createNewSubTask(SubTask task, EpicTask epicTask) {
        task.setEpicId(epicTask.getUniqId());
        epicTask.getSubTasks().put(++counterId, task);
        task.setStatus(TaskStatus.NEW);
        task.setUniqId(counterId);
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getUniqId())) {
            tasks.get(task.getUniqId()).setName(task.getName());
            tasks.get(task.getUniqId()).setDescription(task.getDescription());
            tasks.get(task.getUniqId()).setStatus(task.getStatus());
        }
    }

    @Override
    public void updateEpicTask(EpicTask task) {
        if (epicTasks.containsKey(task.getUniqId())) {
            epicTasks.get(task.getUniqId()).setName(task.getName());
            epicTasks.get(task.getUniqId()).setDescription(task.getDescription());
        }
    }

    @Override
    public void updateSubTask(SubTask task) {
        for (EpicTask epic : epicTasks.values()) {
            if (epic.getSubTasks().containsKey(task.getUniqId())) {
                epic.getSubTasks().get(task.getUniqId()).setName(task.getName());
                epic.getSubTasks().get(task.getUniqId()).setDescription(task.getDescription());
                epic.getSubTasks().get(task.getUniqId()).setStatus(task.getStatus());
                epic.checkStatus(epic);
            }
        }
    }

    @Override
    public void removeTask(int uniqId) {
        tasks.remove(uniqId);
        historyManager.remove(uniqId);
    }

    @Override
    public void removeEpicTask(int uniqId) {
        epicTasks.remove(uniqId);
        historyManager.remove(uniqId);
    }

    @Override
    public void removeSubTask(int uniqId) {
        for (EpicTask epic : epicTasks.values()) {
            if (epic.getSubTasks().containsKey(uniqId)) {
                epic.getSubTasks().remove(uniqId);
                historyManager.remove(uniqId);
                epic.checkStatus(epic);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}
