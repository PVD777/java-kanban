package model;

import service.TaskType;

public class SubTask extends Task {
    private int epicId;
    final TaskType type = TaskType.SUBTASK;

    public SubTask(String name, String description, EpicTask task) {
        super(name, description);
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
        return getId() + "," + type + "," + getName() + "," + getStatus() +"," + getDescription() + ',' + epicId;
    }
}
