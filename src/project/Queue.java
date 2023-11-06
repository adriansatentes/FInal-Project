package project;

import javax.swing.*;

public class Queue {
    private String tasks[];
    private int front, rear, capacity;

    public Queue() {
        capacity = 5;
        tasks = new String[capacity];
        front = rear = -1;
    }

    public Queue(int capacity) {
        this.capacity = capacity;
        tasks = new String[capacity];
        front = rear = -1;
    }

    public boolean isEmpty() {
        return rear == -1;
    }

    public boolean isFull() {
        return rear == capacity - 1;
    }

    private void errorMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Full", JOptionPane.ERROR_MESSAGE);
    }

    public void enqueue(String data) {
        if (isFull()) {
            errorMessage("Queue is full");
        } else {
            tasks[++rear] = data;
            front = 0;
        }
    }

    public String dequeue() {
        String val = null;
        if (isEmpty()) {
            errorMessage("Queue is Empty");
        } else {
            val = tasks[front];
            for (int i = 0; i < rear; i++) {
                tasks[i] = tasks[i + 1];
            }
            rear--;
        }
        return val;
    }

    public String display() {
        StringBuilder hold = new StringBuilder();
        if (!isEmpty()) {
            for (int i = front; i <= rear; i++) {
                hold.append(i + 1).append(". ").append(tasks[i]).append("\n");
            }
        } else {
            hold.append("Queue is empty");
        }
        return hold.toString();
    }

    public String search(String searchTerm) {
        for (int i = front; i <= rear; i++) {
            if (tasks[i].equalsIgnoreCase(searchTerm)) {
                return tasks[i];
            }
        }
        return null;
    }

    public boolean delete(String taskName) {
        boolean deleted = false;
        int foundIndex = -1;

        for (int i = front; i <= rear; i++) {
            if (tasks[i].equalsIgnoreCase(taskName)) {
                foundIndex = i;
                break;
            }
        }

        if (foundIndex != -1) {
            for (int i = foundIndex; i < rear; i++) {
                tasks[i] = tasks[i + 1];
            }
            rear--;

            deleted = true;
        }

        return deleted;
    }

    public boolean contains(String taskToFind) {
        if (isEmpty()) {
            return false;
        }

        for (int i = front; i <= rear; i++) {
            if (tasks[i].equalsIgnoreCase(taskToFind)) {
                return true;
            }
        }
        return false;
    }
}
