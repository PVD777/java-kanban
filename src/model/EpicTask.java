package model;

import service.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {
    private final List<Integer> subTasksID = new ArrayList<>();
    final TaskType type = TaskType.EPIC;
    private LocalDateTime endTime = LocalDateTime.now();

    public EpicTask(String name, String description) {
        super(name, description);
    }

/*    public EpicTask(String name, String description, String startTime) {
        super(name, description);
        setStartTime(LocalDateTime.parse(startTime, formatter));

    }*/


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
        if (getStartTime() != null) {
            return getId() + "," + type + "," + getName() + "," + getStatus() + "," + getDescription() + "," +
                    getStartTime().format(formatter) + "," + getTaskDuration().toMinutes() + ',';
        } else { return getId() + "," + type + "," + getName() + "," + getStatus() + "," + getDescription() + "," +
                "," + "," + getTaskDuration().ofMinutes(0) + ',';

        }

    }
}
