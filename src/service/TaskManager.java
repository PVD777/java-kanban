package service;

import model.*;

import java.util.ArrayList;

public interface TaskManager{

    // 3 метода для возврата задач/эпиков/сабов
    ArrayList<Task> findAllTasks();
    ArrayList<EpicTask> findAllEpicTasks();

    ArrayList<SubTask> findAllSubTasks();

    //Вернуть список подзадач конкретного эпика
    ArrayList<SubTask> findTasksOfEpic(EpicTask epic);

    // Методы для очистки от обычных задач/эпиков, сабов
    void clearTask();

    void clearEpicTask();

    void clearSubTask();

    // Получение по АйДи задачи, эпика, саба
    Task getTask(int uniqId);

    EpicTask getEpicTask(int uniqId);

    SubTask getSubTask(int uniqId);

    // создание задачи, эпика, саба
    void createNewTask(Task task);
    void createNewEpicTask(EpicTask task);
    void createNewSubTask(SubTask task, EpicTask epicTask);

    // обновление обычной задачи, эпика, саба
    void updateTask(Task task);

    void updateEpicTask(EpicTask task);

    void updateSubTask(SubTask task);

    // удалить обычное задание, эпик, саб
    void removeTask(int uniqId);

    void removeEpicTask(int uniqId);

    void removeSubTask(int uniqId);


}
