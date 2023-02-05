package model;

import service.TaskType;

public class SubTask extends Task {
    private int epicId;
    final TaskType type = TaskType.SUBTASK;

    public SubTask(String name, String description, EpicTask task) {
        super(name, description);
        epicId = task.getId();
    }

    public SubTask(String name, String description, String startTime, long duration, EpicTask task) {
        super(name, description, startTime, duration);
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
                (getStartTime() != null ? getStartTime().format(formatter) : "") +
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
