import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    int counterID = 0; // счетчик для номеров задач
    String[] taskStatus = new String[]{"NEW", "IN_PROGRESS", "DONE"};


    //Коллекции для хранания Задач и Эпиков.
    HashMap<Integer, Task> tasks = new HashMap<>();
    // Подзадачи эпиков хранятся в полях самого эпика
    HashMap<Integer, EpicTask> epicTasks = new HashMap<>();

    // Вернуть список обычных задач
    ArrayList<Object> findAllTasks() {
        ArrayList<Object> tasksList = new ArrayList<>();
        for (Integer id : tasks.keySet()) {
            tasksList.add(tasks.get(id));
        }
        return tasksList;
    }

    // Вернуть список Эпиков
    ArrayList<Object> findAllEpicTasks() {
        ArrayList<Object> epicTasksList = new ArrayList<>();
        for (Integer id : epicTasks.keySet()) {
            epicTasksList.add(epicTasks.get(id));
        }
        return epicTasksList;
    }

    // Вернуть список подзадач
    ArrayList<Object> findAllSubTasks() {
        ArrayList<Object> subTasksList = new ArrayList<>();
        for (EpicTask epicTask : epicTasks.values()) {
            for (Integer id : epicTask.subTasks.keySet()) {
                subTasksList.add(epicTask.subTasks.get(id));
            }
        }
        return subTasksList;
    }

    //Вернить список подзадач конкретного эпика
    ArrayList<Object> findTasksOfEpic(EpicTask epic) {
        ArrayList<Object> subTasksList = new ArrayList<>();
        for (Integer id : epic.subTasks.keySet()) {
            subTasksList.add(epic.subTasks.get(id));
        }
        return subTasksList;
    }

    // Очистка от обычных задач
    void clearTask() {
        tasks.clear();
    }

    // Очистка от Эпиков ( и соответственно подзадач)
    void clearEpicTask() {
        epicTasks.clear();
    }

    // Очистка только списка подзадач
    void clearSubTask() {
        for (EpicTask task : epicTasks.values()) {
            task.subTasks.clear();
        }
    }

    // Получение обычной задачи по номеру
    Task getUniqTask(int uniqID) {
        if (tasks.containsKey(uniqID)) {
            return tasks.get(uniqID);
        }
        System.out.println("Нет задачи с таким номером");
        return null;
    }

    // Получение эпика по номеру
    EpicTask getUniqEpicTask(int uniqID) {
        if (epicTasks.containsKey(uniqID)) {
            return epicTasks.get(uniqID);
        }
        System.out.println("Нет задачи с таким номером");
        return null;
    }

    // Получение подзадачи по номеру
    SubTask getUniqSubTask(int uniqID) {
        for (EpicTask task : epicTasks.values()) {
            if (task.subTasks.containsKey(uniqID)) {
                return task.subTasks.get(uniqID);
            }
        }
        System.out.println("Нет задачи с таким номером");
        return null;
    }

    // создание задачи
    void createNewTask(Task task) {
        tasks.put(++counterID, task);
        task.setStatus(taskStatus[0]);
        task.setUniqID(counterID);
    }

    // создание эпика
    void createNewEpicTask(EpicTask task) {
        epicTasks.put(++counterID, task);
        task.setStatus(taskStatus[0]);
        task.setUniqID(counterID);
    }

    // создание подзадачи
    void createNewSubTask(SubTask task, EpicTask epicTask) {
        task.epicID = epicTask.getUniqID();
        epicTask.subTasks.put(++counterID, task);
        task.setStatus(taskStatus[0]);
        task.setUniqID(counterID);
    }
    // обновление обычной задачи, параметры метода - АйДи номер задачи, новое имя, новое описание и новый статус
    void updateTask(int uniqID, String name, String description, String status) {
        if (tasks.containsKey(uniqID)) {
            tasks.get(uniqID).name = name;
            tasks.get(uniqID).description = description;
            tasks.get(uniqID).setStatus(status);
        } else {
            System.out.println("Нет задачи с таким номером");
        }
    }
    // Обновление эпика, параметры метода - АйДи номер задачи, новое имя, новое описание
    void updateEpicTask(int uniqID, String name, String description) {
        if (epicTasks.containsKey(uniqID)) {
            epicTasks.get(uniqID).name = name;
            epicTasks.get(uniqID).description = description;
        } else {
            System.out.println("Нет задачи с таким номером");
        }
    }

    // Обновление эпика, параметры метода - АйДи номер задачи, новое имя, новое описание и новый статус
    void updateSubTask(int uniqID, String name, String description, String status) {
        for (EpicTask task : epicTasks.values()) {
            if (task.subTasks.containsKey(uniqID)) {
                task.subTasks.get(uniqID).name = name;
                task.subTasks.get(uniqID).description = description;
                task.subTasks.get(uniqID).setStatus(status);

                // Тут должно происходить управление статусом эпика. Решения умнее я не придумал чем вбить статусы в одну
                // строку, и искать по наименованию
                String subStatus = "";
                for (SubTask subTask : task.subTasks.values()) {
                    subStatus += subTask.getStatus();
                }
                if (!subStatus.contains(taskStatus[0]) && (!subStatus.contains(taskStatus[1]))) {
                    task.setStatus(taskStatus[2]);
                } else if (!subStatus.contains(taskStatus[2]) && (!subStatus.contains(taskStatus[1]))) {
                    task.setStatus(taskStatus[0]);
                } else {
                    task.setStatus(taskStatus[1]);
                }
            } else {
                System.out.println("Нет задачи с таким номером");
            }
        }
    }

    // удалить обычное задание
    void removeTask(int uniqID) {
        if (tasks.containsKey(uniqID)) {
            tasks.remove(uniqID);
        } else {
            System.out.println("Нет задачи с таким номером");
        }
    }

    //удалить эпик
    void removeEpicTask(int uniqID) {
        if (epicTasks.containsKey(uniqID)) {
            epicTasks.remove(uniqID);
        } else {
            System.out.println("Нет задачи с таким номером");
        }
    }

    //удалить подзадачу
    void removeSubTask(int uniqID) {
        for (EpicTask task : epicTasks.values()) {
            if (task.subTasks.containsKey(uniqID)) {
                task.subTasks.remove(uniqID);
                // ниже происходит такое же костыльное обновление статуса эпика.
                String status = "";
                for (SubTask subTask : task.subTasks.values()) {
                    status += subTask.getStatus();
                }
                if (!status.contains(taskStatus[0]) && (!status.contains(taskStatus[1]))) {
                    task.setStatus(taskStatus[2]);
                } else if (!status.contains(taskStatus[2]) && (!status.contains(taskStatus[1]))) {
                    task.setStatus(taskStatus[0]);
                } else {
                    task.setStatus(taskStatus[1]);
                }

            } else {
                System.out.println("Нет задачи с таким номером");
            }
        }
    }
}
