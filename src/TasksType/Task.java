package TasksType;

import Utility.TaskStatus;

public class Task {
    private int uniqId;
    private String name;
    private String description;
    private TaskStatus status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getUniqId() {
        return uniqId;
    }


    public void setUniqId(int uniqID) {
        this.uniqId = uniqID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "TasksType.Task{" +
                "uniqID=" + uniqId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

