package service;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {


    ArrayList<Task> findAllTasks();

    ArrayList<EpicTask> findAllEpicTasks();

    ArrayList<SubTask> findAllSubTasks();


    ArrayList<SubTask> findTasksOfEpic(EpicTask epic);


    void clearTasks() throws IOException;

    void clearEpicTasks() throws IOException;

    void clearSubTasks() throws IOException;


    Task getTask(int uniqId) throws IOException;

    EpicTask getEpicTask(int uniqId) throws IOException;

    SubTask getSubTask(int uniqId) throws IOException;


    void createNewTask(Task task) throws IOException;

    void createNewEpicTask(EpicTask task) throws IOException;

    void createNewSubTask(SubTask task) throws IOException;


    void updateTask(Task task) throws IOException;

    void updateEpicTask(EpicTask task) throws IOException;

    void updateSubTask(SubTask task) throws IOException;

    void removeTask(int uniqId) throws IOException;

    void removeEpicTask(int uniqId) throws IOException;

    void removeSubTask(int uniqId) throws IOException;

    List<Task> getHistory();
}
