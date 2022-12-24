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
    private final HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    private final HashMap <Integer, SubTask> subTasks = new HashMap<>();

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
        for (Integer id : subTasks.keySet()) {
            subTasksList.add(subTasks.get(id));
        }
        return subTasksList;
    }

    @Override
    public ArrayList<SubTask> findTasksOfEpic(EpicTask epic) {
        ArrayList<SubTask> subTasksList = new ArrayList<>();
        for (Integer id : epic.getSubTasksID()) {
            subTasksList.add(subTasks.get(id));
        }
        return subTasksList;
    }

    @Override
    public void clearTasks() {
        List<Task> history = historyManager.getHistory();
        for (Integer id : tasks.keySet()) {
            if (history.contains(tasks.get(id))) {
                historyManager.remove(id);
            }
        }
        tasks.clear();
    }

    @Override
    public void clearEpicTasks() {
        List<Task> history = historyManager.getHistory();
        for (Integer id : epicTasks.keySet()) {
            if (history.contains(epicTasks.get(id))) {
                historyManager.remove(id);
            }
        }
        epicTasks.clear();
    }

    @Override
    public void clearSubTasks() {
        List<Task> history = historyManager.getHistory();
        for (Integer id : subTasks.keySet()) {
            if (history.contains(subTasks.get(id))) {
                historyManager.remove(id);
            }
        }
        subTasks.clear();
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
        if (subTasks.containsKey(uniqId)) {
            historyManager.add(subTasks.get(uniqId));
            return subTasks.get(uniqId);
        }
        return null;
    }

    @Override
    public void createNewTask(Task task) {
        tasks.put(++counterId, task);
        task.setStatus(TaskStatus.NEW);
        task.setId(counterId);
    }

    @Override
    public void createNewEpicTask(EpicTask task) {
        epicTasks.put(++counterId, task);
        task.setStatus(TaskStatus.NEW);
        task.setId(counterId);
    }

    public void createNewSubTask(SubTask task) {
        subTasks.put(++counterId, task);
        task.setStatus(TaskStatus.NEW);
        task.setId(counterId);
        epicTasks.get(task.getEpicId()).addSubTasksID(counterId);
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpicTask(EpicTask task) {
        if (epicTasks.containsKey(task.getId())) {
            epicTasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubTask(SubTask task) {
        if (subTasks.containsKey(task.getId())) {
            subTasks.put(task.getId(), task);
            checkEpicStatus(task.getEpicId());
        }

    }

    public void checkEpicStatus (Integer epicId) {
        boolean isNew = false;
        boolean isInProgress = false;
        boolean isDone = false;
        for (Integer id : epicTasks.get(epicId).getSubTasksID()) {
            if (!isNew && (subTasks.get(id).getStatus() == (TaskStatus.NEW))) isNew = true;
            if (!isInProgress && subTasks.get(id).getStatus() == (TaskStatus.IN_PROGRESS)) isInProgress = true;
            if (!isDone && subTasks.get(id).getStatus() == (TaskStatus.DONE)) isDone = true;

            if (isNew && !isInProgress && !isDone) {
                epicTasks.get(epicId).setStatus(TaskStatus.NEW);
            } else if ((!isNew && !isInProgress && isDone)) {
                epicTasks.get(epicId).setStatus(TaskStatus.DONE);
            } else {
                epicTasks.get(epicId).setStatus(TaskStatus.IN_PROGRESS);
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
        for (Integer id : epicTasks.get(uniqId).getSubTasksID()) {
            subTasks.remove(id);
            historyManager.remove(id);
        }
        epicTasks.remove(uniqId);
        historyManager.remove(uniqId);
    }

    @Override
    public void removeSubTask(int uniqId) {
        int tmp = subTasks.get(uniqId).getEpicId();
        subTasks.remove(uniqId);
        epicTasks.get(tmp).removeFromSubTasksID(uniqId);
        historyManager.remove(uniqId);

        checkEpicStatus(tmp);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}
