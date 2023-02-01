package service;


import model.EpicTask;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {


    private int counterId = 0;
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    protected TreeSet<Task> prioritizedTasks = new TreeSet<>((task1, task2) -> {
        if (task1.getStartTime().isBefore(task2.getStartTime())) return -1;
        else if ((task1.getStartTime().isAfter(task2.getStartTime())) ||
                (task1.getStartTime().isEqual(LocalDateTime.now()))) return 1;
        else return 0;
    });

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
            prioritizedTasks.remove(tasks.get(id));
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
            prioritizedTasks.remove(epicTasks.get(id));
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
            prioritizedTasks.remove(subTasks.get(id));
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
        if (!checkForConflux(task)) {
            task.setId(++counterId);
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        }

    }

    @Override
    public void createNewEpicTask(EpicTask task) {
        if (!checkForConflux(task)) {
            setEpicDuration(task);
            task.setId(++counterId);
            epicTasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        }

    }

    public void createNewSubTask(SubTask task) {
        if (!checkForConflux(task)) {
            task.setId(++counterId);
            subTasks.put(task.getId(), task);
            epicTasks.get(task.getEpicId()).addSubTasksID(task.getId());
            setEpicDuration(epicTasks.get(task.getEpicId()));
            prioritizedTasks.add(task);
        }


    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            prioritizedTasks.remove(tasks.get(task.getId()));
            prioritizedTasks.add(task);
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpicTask(EpicTask task) {
        if (epicTasks.containsKey(task.getId())) {
            prioritizedTasks.remove(epicTasks.get(task.getId()));
            prioritizedTasks.add(task);
            epicTasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubTask(SubTask task) {
        if (subTasks.containsKey(task.getId())) {
            prioritizedTasks.remove(subTasks.get(task.getId()));
            prioritizedTasks.add(task);
            subTasks.put(task.getId(), task);
            checkEpicStatus(task.getEpicId());
            setEpicDuration(epicTasks.get(task.getEpicId()));

        }

    }

    private void checkEpicStatus (Integer epicId) {
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

    private boolean checkForConflux(Task task) {
        boolean isConflux = false;
        if (task.getStartTime().isEqual(LocalDateTime.now())) return isConflux;

        for (Task sortedTask : prioritizedTasks) {
            if ((task.getStartTime().isAfter(sortedTask.getStartTime()) &&
                    task.getStartTime().isBefore(sortedTask.getFinishedTime())) ||
                    (task.getFinishedTime().isAfter(sortedTask.getStartTime()) &&
                            task.getFinishedTime().isBefore(sortedTask.getFinishedTime()))) isConflux = true;
        }
        return isConflux;
    }

    private void setEpicDuration(EpicTask task) {
        Duration epicDuration = Duration.ofMinutes(0);
        for (int subNum : task.getSubTasksID()) {
            epicDuration = task.getTaskDuration().plus(subTasks.get(subNum).getTaskDuration());
        }
        task.setTaskDuration(epicDuration);
    }


    @Override
    public void removeTask(int uniqId) {
        prioritizedTasks.remove(tasks.get(uniqId));
        tasks.remove(uniqId);
        historyManager.remove(uniqId);
    }

    @Override
    public void removeEpicTask(int uniqId) {
        try {
            for (Integer id : epicTasks.get(uniqId).getSubTasksID()) {
                subTasks.remove(id);
                historyManager.remove(id);
            }
            prioritizedTasks.remove(epicTasks.get(uniqId));
            epicTasks.remove(uniqId);
            historyManager.remove(uniqId);
        } catch (NullPointerException e) {

        }
    }

    @Override
    public void removeSubTask(int uniqId) {
        try {
            int tmp = subTasks.get(uniqId).getEpicId();
            prioritizedTasks.remove(subTasks.get(uniqId));
            subTasks.remove(uniqId);
            epicTasks.get(tmp).removeFromSubTasksID(uniqId);
            historyManager.remove(uniqId);

            checkEpicStatus(tmp);
        } catch (NullPointerException e) {

        }

    }

    public void setCounterId(int counterId) {
        this.counterId = counterId;
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public void setHistory(List<Integer> historyList) {

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
