package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class InMemoryHistoryManager implements HistoryManager {
    private CustumLinkedList linkedTasksHistory = new CustumLinkedList();


    @Override
    public void add(Task task) {
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
    class CustumLinkedList {

        public HashMap<Integer, Node<Task>> mapHistory = new HashMap<>();

        public Node<Task> head;
        public Node<Task> tail;

        public void linkLast(Task task) {
            Node<Task> oldTail = tail;
            Node<Task> newNode = new Node<>(oldTail, task, null);
            tail = newNode;
            if (oldTail == null)
                head = newNode;
            else
                oldTail.setNext(newNode);
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
            Node prevNode = node.getPrev();
            Node nextNode = node.getNext();
            if (prevNode != null) {
                prevNode.setNext(nextNode);
            } else {
                head = nextNode;
            }
            if (nextNode != null) {
                nextNode.setPrev(prevNode);
            } else {
                tail = prevNode;
            }
            mapHistory.remove(node.getData().getId());
        }


    }

}


