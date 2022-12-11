package model;

import service.TaskStatus;

import java.util.HashMap;

public class EpicTask extends Task {
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public EpicTask(String name, String description) {
        super(name, description);
    }

    /*Метод для проверки и изменения при необходимости статуса эпика
      Принмает эпик, проверяет на наличие разных статусов подзадач, присваивает статус эпику*/
    public void checkStatus(EpicTask epic) {
        boolean isNew = false;
        boolean isInProgress = false;
        boolean isDone = false;
        for (SubTask subTask : epic.subTasks.values()) {
            if ((!isNew) && (subTask.getStatus() == (TaskStatus.NEW))) isNew = true;
            if (!isInProgress && subTask.getStatus() == (TaskStatus.IN_PROGRESS)) isInProgress = true;
            if (!isDone && subTask.getStatus() == (TaskStatus.DONE)) isDone = true;
            if (isNew && !isInProgress && !isDone) {
                epic.setStatus(TaskStatus.NEW);
            } else if ((!isNew && !isInProgress && isDone)) {
                epic.setStatus(TaskStatus.DONE);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public String toString() {
        return "TasksType.EpicTask{" +
                "uniqID=" + getUniqId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", subTasks='" + subTasks + '\'' +
                '}';
    }
}
