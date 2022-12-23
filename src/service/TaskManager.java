package service;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {


    ArrayList<Task> findAllTasks();

    ArrayList<EpicTask> findAllEpicTasks();

    ArrayList<SubTask> findAllSubTasks();


    ArrayList<SubTask> findTasksOfEpic(EpicTask epic);


    void clearTasks();

    void clearEpicTasks();

    void clearSubTasks();


    Task getTask(int uniqId);

    EpicTask getEpicTask(int uniqId);

    SubTask getSubTask(int uniqId);


    void createNewTask(Task task);

    void createNewEpicTask(EpicTask task);

    void createNewSubTask(SubTask task);


    void updateTask(Task task);

    void updateEpicTask(EpicTask task);

    void updateSubTask(SubTask task);

    void checkEpicStatus (Integer id);


    void removeTask(int uniqId);

    void removeEpicTask(int uniqId);

    void removeSubTask(int uniqId);

    List<Task> getHistory();
}
