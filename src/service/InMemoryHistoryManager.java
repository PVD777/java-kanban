package service;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private static ArrayList<Task> tasksHistory = new ArrayList<>();
    @Override
    public void add(Task task) {
        if (tasksHistory.size() < 10) {
            tasksHistory.add(task);
        } else {
            tasksHistory.remove(0);
            tasksHistory.add(task);
        }
    }
    public ArrayList<Task> getHistory() {
        return tasksHistory;
    }
}
