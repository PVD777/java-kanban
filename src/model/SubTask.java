package model;

import service.TaskType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, EpicTask task) {
        super(name, description);
        type = TaskType.SUBTASK;
        epicId = task.getId();
    }

    public SubTask(String name, String description, String startTime, Long duration, EpicTask task) {
        super(name, description, startTime, duration);
        type = TaskType.SUBTASK;
        epicId = task.getId();
    }
    public SubTask(String name, String description, LocalDateTime startTime, Long duration, EpicTask task) {
        super(name, description, startTime, duration);
        type = TaskType.SUBTASK;
        epicId = task.getId();
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return getId() + "," + type + "," + getName() + "," + getStatus() + "," + getDescription() + "," +
                (getStartTime() != null ? getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy|HH:mm")) : "") + "," +
                (getTaskDuration() != null ? getTaskDuration().toMinutes() : "") + ',' + epicId;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
