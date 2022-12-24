package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class InMemoryHistoryManager implements HistoryManager {
    private CustumLinkedList<Task> linkedTasksHistory = new CustumLinkedList<>();


    @Override
    public void add(Task task) {
        if (linkedTasksHistory.size() >= 10) {
            linkedTasksHistory.removeFirstNode();
        }
        if (linkedTasksHistory.mapHistory.containsKey(task.getId())) {
            linkedTasksHistory.removeNode(linkedTasksHistory.mapHistory.get(task.getId()));
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
    class CustumLinkedList<T> {

        public HashMap<Integer, Node<Task>> mapHistory = new HashMap<>();

        public Node<Task> head;
        public Node<Task> tail;
        private int size = 0;


        public void linkLast(Task task) {
            Node<Task> oldTail = tail;
            Node<Task> newNode = new Node<>(oldTail, task, null);
            tail = newNode;
            if (oldTail == null)
                head = newNode;
            else
                oldTail.setNext(newNode);
            size++;
            mapHistory.put(task.getId(), newNode);
        }

        public ArrayList<Task> getTasks() {
            ArrayList<Task> taskHistory = new ArrayList<>();
            Node<Task> tmp = head;
            while (tmp != null) {
                taskHistory.add(tmp.getData());
                tmp = tmp.getNext();
            }
            return taskHistory;
        }

        public void removeNode(Node node) {
            if (head == null || node == null) {
                return;
            }
            if (head == node) {
                head = node.getNext();
            }
            if (node.getNext() != null) {
                node.getNext().setPrev(node.getPrev());
            }
            if (node.getPrev() != null) {
                node.getPrev().setNext(node.getNext());
            }
            size--;
            mapHistory.remove(node.getData().getId());
            return;
        }

        public void removeFirstNode() {
            if (head != null) {
                mapHistory.remove(head.getData().getId());
                Node<Task> temp = head;
                head = head.getNext();
                temp = null;
            }
            if (head != null)
                head.setPrev(null);
            size--;
        }

        public int size() {
            return this.size;
        }
    }

}


