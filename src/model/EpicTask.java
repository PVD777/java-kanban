package model;

import service.TaskType;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class EpicTask extends Task {
    private final List<Integer> subTasksID = new ArrayList<>();
    final TaskType type = TaskType.EPIC;
    private LocalDateTime endTime;
    public EpicTask(String name, String description) {
        super(name, description);
    }

    public List<Integer> getSubTasksID() {
        return subTasksID;
    }

    public void addSubTasksID(Integer id) {
        subTasksID.add(id);
    }


    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void removeFromSubTasksID(Integer subId) {
        subTasksID.remove(subId);

    }

    @Override
    public String toString() {
        return getId() + "," + type + "," + getName() + "," + getStatus() + "," + getDescription() + "," +
                (getStartTime() != null ? getStartTime().format(formatter) : "") + "," +
                (getTaskDuration() != null ? getTaskDuration().toMinutes() : "") +  ',';
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
