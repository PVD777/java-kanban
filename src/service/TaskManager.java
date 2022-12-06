package service;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static int counterId = 0; // счетчик для номеров задач

    //Коллекции для хранания Задач и Эпиков.
    HashMap<Integer, Task> tasks = new HashMap<>();
    // Подзадачи эпиков хранятся в полях самого эпика
    HashMap<Integer, EpicTask> epicTasks = new HashMap<>();

    // 3 метода для возврата задач/эпиков/сабов
    public ArrayList<Task> findAllTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();
        for (Integer id : tasks.keySet()) {
            tasksList.add(tasks.get(id));
        }
        return tasksList;
    }

    public ArrayList<EpicTask> findAllEpicTasks() {
        ArrayList<EpicTask> epicTasksList = new ArrayList<>();
        for (Integer id : epicTasks.keySet()) {
            epicTasksList.add(epicTasks.get(id));
        }
        return epicTasksList;
    }

    public ArrayList<SubTask> findAllSubTasks() {
        ArrayList<SubTask> subTasksList = new ArrayList<>();
        for (EpicTask epicTask : epicTasks.values()) {
            for (Integer id : epicTask.getSubTasks().keySet()) {
                subTasksList.add(epicTask.getSubTasks().get(id));
            }
        }
        return subTasksList;
    }

    //Вернить список подзадач конкретного эпика
    public ArrayList<SubTask> findTasksOfEpic(EpicTask epic) {
        ArrayList<SubTask> subTasksList = new ArrayList<>();
        for (Integer id : epic.getSubTasks().keySet()) {
            subTasksList.add(epic.getSubTasks().get(id));
        }
        return subTasksList;
    }

    // Методы для очистки от обычных задач/эпиков, сабов
    public void clearTask() {
        tasks.clear();
    }

    public void clearEpicTask() {
        epicTasks.clear();
    }

    public void clearSubTask() {
        for (EpicTask task : epicTasks.values()) {
            task.getSubTasks().clear();
        }
    }

    // Получение по АйДи задачи, эпика, саба
    public Task getUniqTask(int uniqId) {
        if (tasks.containsKey(uniqId)) {
            return tasks.get(uniqId);
        }
        System.out.println("Нет задачи с таким номером");
        return null;
    }

    public EpicTask getUniqEpicTask(int uniqId) {
        if (epicTasks.containsKey(uniqId)) {
            return epicTasks.get(uniqId);
        }
        System.out.println("Нет задачи с таким номером");
        return null;
    }

    public SubTask getUniqSubTask(int uniqId) {
        for (EpicTask task : epicTasks.values()) {
            if (task.getSubTasks().containsKey(uniqId)) {
                return task.getSubTasks().get(uniqId);
            }
        }
        System.out.println("Нет задачи с таким номером");
        return null;
    }

    // создание задачи, эпика, саба
    public void createNewTask(Task task) {
        tasks.put(++counterId, task);
        task.setStatus(TaskStatus.NEW);
        task.setUniqId(counterId);
    }

    public void createNewEpicTask(EpicTask task) {
        epicTasks.put(++counterId, task);
        task.setStatus(TaskStatus.NEW);
        task.setUniqId(counterId);
    }

    public void createNewSubTask(SubTask task, EpicTask epicTask) {
        task.setEpicId(epicTask.getUniqId());
        epicTask.getSubTasks().put(++counterId, task);
        task.setStatus(TaskStatus.NEW);
        task.setUniqId(counterId);
    }

    // обновление обычной задачи, эпика, саба
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getUniqId())) {
            tasks.get(task.getUniqId()).setName(task.getName());
            tasks.get(task.getUniqId()).setDescription(task.getDescription());
            tasks.get(task.getUniqId()).setStatus(task.getStatus());
        } else {
            System.out.println("Нет такой задачи");
        }
    }

    public void updateEpicTask(EpicTask task) {
        if (epicTasks.containsKey(task.getUniqId())) {
            epicTasks.get(task.getUniqId()).setName(task.getName());
            epicTasks.get(task.getUniqId()).setDescription(task.getDescription());
        } else {
            System.out.println("Нет такого эпика");
        }
    }

    public void updateSubTask(SubTask task) {
        for (EpicTask epic : epicTasks.values()) {
            if (epic.getSubTasks().containsKey(task.getUniqId())) {
                epic.getSubTasks().get(task.getUniqId()).setName(task.getName());
                epic.getSubTasks().get(task.getUniqId()).setDescription(task.getDescription());
                epic.getSubTasks().get(task.getUniqId()).setStatus(task.getStatus());
                epic.checkStatus(epic);
            } else {
                System.out.println("Нет такй подзадачи");
            }
        }
    }

    // удалить обычное задание, эпик, саб
    public void removeTask(int uniqId) {
        if (tasks.containsKey(uniqId)) {
            tasks.remove(uniqId);
        } else {
            System.out.println("Нет задачи с таким номером");
        }
    }

    public void removeEpicTask(int uniqId) {
        if (epicTasks.containsKey(uniqId)) {
            epicTasks.remove(uniqId);
        } else {
            System.out.println("Нет задачи с таким номером");
        }
    }

    public void removeSubTask(int uniqId) {
        for (EpicTask epic : epicTasks.values()) {
            if (epic.getSubTasks().containsKey(uniqId)) {
                epic.getSubTasks().remove(uniqId);
                epic.checkStatus(epic);
            } else {
                System.out.println("Нет задачи с таким номером");
            }
        }
    }


}
