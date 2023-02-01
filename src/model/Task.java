package model;

import service.TaskStatus;
import service.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Task {
    private int id;
    private String name;
    private String description;
    private TaskStatus status;
    private final TaskType type = TaskType.TASK;
    private Duration duration = Duration.ofMinutes(0);
    private LocalDateTime startTime = LocalDateTime.now();
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy|HH:mm");

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
    }

    public Task(String name, String description, String startTime, long durationInMinutes) {
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
        this.startTime = LocalDateTime.parse(startTime, formatter);
        this.duration = Duration.ofMinutes(durationInMinutes);
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

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
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

    public LocalDateTime getFinishedTime() {
        return startTime.plus(duration);
    }

    @Override
    public String toString() {
        return id + "," + type + "," + name + "," + status + "," + description + ',' +
                startTime.format(formatter) + "," + duration.toMinutes() + ",";
    }
}

