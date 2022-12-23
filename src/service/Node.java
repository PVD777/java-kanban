package service;

import model.Task;

public class Node<T> {
    private Node<Task> prev;
    private Task data;
    private Node<Task> next;

    public Node(Node<Task> prev, Task data, Node<Task> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    public void setPrev(Node<Task> prev) {
        this.prev = prev;
    }

    public void setData(Task data) {
        this.data = data;
    }

    public void setNext(Node<Task> next) {
        this.next = next;
    }

    public Node<Task> getPrev() {
        return prev;
    }

    public Task getData() {
        return data;
    }

    public Node<Task> getNext() {
        return next;
    }

}
