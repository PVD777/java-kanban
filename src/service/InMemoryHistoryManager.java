package service;

import model.Task;

import java.util.LinkedList;


public class InMemoryHistoryManager implements HistoryManager {

    private static final LinkedList<Task> tasksHistory = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (tasksHistory.size() >= 10) {
            tasksHistory.remove(0);
        }
        tasksHistory.add(task);

    }

    public LinkedList<Task> getHistory() {
        return tasksHistory;
    }
}
