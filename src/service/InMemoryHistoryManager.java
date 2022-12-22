package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class InMemoryHistoryManager implements HistoryManager {
        private static final CustumLinkedList<Task> linkedTasksHistory = new CustumLinkedList<>();


    @Override
    public void add(Task task) {
        if (linkedTasksHistory.mapHistory.containsKey(task.getUniqId())) {
            linkedTasksHistory.removeNode(linkedTasksHistory.mapHistory.get(task.getUniqId()));
        }
        linkedTasksHistory.linkLast(task);
    }

    @Override
    public void remove(int id) {
        if (linkedTasksHistory.mapHistory.containsKey(id)) {
            linkedTasksHistory.removeNode(linkedTasksHistory.mapHistory.get(id));
            linkedTasksHistory.mapHistory.remove(id);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return linkedTasksHistory.getTasks();
    }
}

class CustumLinkedList<T> {

    public HashMap<Integer, Node<Task>> mapHistory = new HashMap<>();

    public Node<Task> head;
    public Node<Task> tail;
    private int size = 0;


    public void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
        size++;
        mapHistory.putIfAbsent(task.getUniqId(), newNode);
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> taskHistory = new ArrayList<>();
        Node<Task> tmp = head;
        while (tmp != null) {
            taskHistory.add(tmp.data);
            tmp = tmp.next;
        }
        return taskHistory;
    }

    public void removeNode(Node node) {
        if (head == null || node == null) {
            return;
        }
        if (head == node) {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        }
        return;
    }

    public void removeFirstNode() {
        if (head != null) {
            Node<Task> temp = head;
            head = head.next;
            temp = null;
        }
        if (head != null)
            head.prev = null;
    }

    public int size() {
        return this.size;
    }
}
