package service;

import model.Task;

public class Node<T> {
    public Node<Task> prev;
    public Task data;
    public Node<Task> next;

    public Node(Node<Task> prev, Task data, Node<Task> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                '}';
    }
}
