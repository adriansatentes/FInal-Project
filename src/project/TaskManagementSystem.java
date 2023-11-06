package project;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class TaskManagementSystem extends JFrame {
    private Queue inProgressTasks = new Queue();
    private Queue completedTasks = new Queue();
    private ArrayList<Task> taskList = new ArrayList<>();

    private JTextArea inProgressTasksTextArea;
    private JTextArea completedTasksTextArea;
    private JTextField searchTextField;

    public TaskManagementSystem() {
        setTitle("TechMach Innovations, LLC.");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon background = new ImageIcon("images.jpg"); 
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setLayout(new BorderLayout());

        setContentPane(backgroundLabel);

        inProgressTasksTextArea = new JTextArea(20, 25);
        inProgressTasksTextArea.setEditable(false);

        completedTasksTextArea = new JTextArea(20, 25);
        completedTasksTextArea.setEditable(false);

        JScrollPane inProgressScrollPane = new JScrollPane(inProgressTasksTextArea);
        JScrollPane completedScrollPane = new JScrollPane(completedTasksTextArea);
        backgroundLabel.add(inProgressScrollPane, BorderLayout.CENTER);
        backgroundLabel.add(completedScrollPane, BorderLayout.EAST);

        JPanel topPanel = new JPanel();
        searchTextField = new JTextField(30);
        JButton searchButton = new JButton("Search Task");

        topPanel.add(new JLabel("Search Task:"));
        topPanel.add(searchTextField);
        topPanel.add(searchButton);

        JPanel centerPanel = new JPanel();
        centerPanel.add(inProgressScrollPane);
        centerPanel.add(completedScrollPane);

        JPanel bottomPanel = new JPanel();
        JButton addButton = new JButton("Add Task");
        JButton completeButton = new JButton("Complete Task");
        JButton deleteButton = new JButton("Delete Task");

        bottomPanel.add(addButton);
        bottomPanel.add(completeButton);
        bottomPanel.add(deleteButton);

        addButton.addActionListener(e -> addTask());
        deleteButton.addActionListener(e -> deleteTask());
        completeButton.addActionListener(e -> completeTask());
        searchButton.addActionListener(e -> searchTask());

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addTask() {
        TaskInputFrame inputForm = new TaskInputFrame(this); 
        inputForm.setVisible(true);
    }

    public void addTaskToList(Task task) {
        boolean isDuplicate = taskList.stream()
                .anyMatch(existingTask -> existingTask.getTaskName().equalsIgnoreCase(task.getTaskName()));
        if (!isDuplicate && !containsSpecialCharactersOrNumbers(task.getTaskName())
                && !containsSpecialCharactersOrNumbers(task.getInstructions())) {
            // Set the due date for the task to today's date
            Date currentDate = new Date();
            task.setDueDate(currentDate);

            taskList.add(task);
            inProgressTasks.enqueue(task.getTaskName());
            updateTaskLists();
        } else {
            JOptionPane.showMessageDialog(this, "Task is invalid or duplicate", "Invalid Task", JOptionPane.WARNING_MESSAGE);
        }
    }


    public boolean containsSpecialCharactersOrNumbers(String input) {
        Pattern pattern = Pattern.compile("[^a-zA-Z\\s]");
        return pattern.matcher(input).find();
    }

    private void searchTask() {
        String searchTerm = searchTextField.getText().trim();
        if (!searchTerm.isEmpty()) {
            Task foundTask = null;
            for (Task task : taskList) {
                if (task.getTaskName().equalsIgnoreCase(searchTerm)) {
                    foundTask = task;
                    break;
                }
            }

            if (foundTask != null) {
                TaskViewFrame taskViewFrame = new TaskViewFrame(foundTask);
                taskViewFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Task not found", "Task Not Found", JOptionPane.WARNING_MESSAGE);
            }
        }

        searchTextField.setText("");
    }

    private void deleteTask() {
        String taskName = searchTextField.getText().trim();
        if (!taskName.isEmpty()) {
            boolean deleted = inProgressTasks.delete(taskName);

            if (!deleted) {
                deleted = completedTasks.delete(taskName);
            }

            if (deleted) {
                taskList.removeIf(task -> task.getTaskName().equals(taskName));
                updateTaskLists();
            } else {
                JOptionPane.showMessageDialog(this, "Task not found", "Task Not Found", JOptionPane.WARNING_MESSAGE);
            }
        }

        searchTextField.setText("");
    }

    private void completeTask() {
        if (!inProgressTasks.isEmpty()) {
            String taskName = inProgressTasks.dequeue();
            completedTasks.enqueue(taskName);
            updateTaskLists();
        } else {
            JOptionPane.showMessageDialog(this, "No tasks in progress to complete", "Task Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void updateTaskInViewFrame(Task task) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getTaskName().equals(task.getTaskName())) {
                taskList.set(i, task);
                Date dueDate = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
                task.setDueDate(dueDate);
                break;
            }
        }
        updateTaskLists(); 

        // Refresh the task queues
        inProgressTasks = new Queue();
        completedTasks = new Queue();
        for (Task t : taskList) {
            if (inProgressTasksTextArea.getText().contains(t.getTaskName())) {
                inProgressTasks.enqueue(t.getTaskName());
            } else if (completedTasksTextArea.getText().contains(t.getTaskName())) {
                completedTasks.enqueue(t.getTaskName());
            }
        }
    }

    private void updateTaskLists() {
        inProgressTasksTextArea.setText("In Progress:\n" + inProgressTasks.display());
        completedTasksTextArea.setText("Completed:\n" + completedTasks.display());

        inProgressTasksTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    Point2D point2D = e.getPoint();
                    int caretPosition = inProgressTasksTextArea.viewToModel2D(point2D);

                    try {
                        int line = inProgressTasksTextArea.getLineOfOffset(caretPosition);
                        int startOffset = inProgressTasksTextArea.getLineStartOffset(line);
                        int endOffset = inProgressTasksTextArea.getLineEndOffset(line);

                        String clickedLine = inProgressTasksTextArea.getText(startOffset, endOffset - startOffset);

                       
                        String[] parts = clickedLine.split("\\. ", 2);
                        if (parts.length == 2) {
                            String taskName = parts[1];
                            showTaskDetails(taskName.trim());
                        }
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void showTaskDetails(String taskName) {
        for (Task task : taskList) {
            if (task.getTaskName().equals(taskName)) {
                TaskViewFrame taskViewFrame = new TaskViewFrame(task);
                taskViewFrame.setVisible(true);
                break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaskManagementSystem taskManager = new TaskManagementSystem();
            taskManager.setVisible(true);
        });
    }
}
