package model;

import service.TaskStatus;
import service.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private int id;
    private String name;
    private String description;
    private TaskStatus status;
    protected TaskType type;
    private Duration duration;
    private LocalDateTime startTime;


    public Task(String name, String description) {
        type = TaskType.TASK;
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
    }

    public Task(String name, String description, String startTime, Long durationInMinutes) {
        type = TaskType.TASK;
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
        if (startTime == null) this.startTime = null;
        else this.startTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("dd.MM.yyyy|HH:mm"));
        if (durationInMinutes == null) duration = null;
        else duration = Duration.ofMinutes(durationInMinutes);
    }

    public Task(String name, String description, LocalDateTime startTime, Long durationInMinutes) {
        type = TaskType.TASK;
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
        if (startTime == null) this.startTime = null;
        else this.startTime = startTime;
        if (durationInMinutes == null) duration = null;
        else duration = Duration.ofMinutes(durationInMinutes);
    }



    public int getId() {
        return id;
    }


    public void setId(int uniqID) {
        this.id = uniqID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
    public void setTaskDuration(Duration taskDuration) {
        this.duration = taskDuration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getTaskDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public String toString() {
        return  id + "," + type + "," + name + "," + status + "," + description + ',' +
                (startTime != null ? startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy|HH:mm")): "") +
                "," + (duration != null ? duration.toMinutes() : "") + ",";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && type == task.type && Objects.equals(duration, task.duration) && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, type, duration, startTime);
    }
}

