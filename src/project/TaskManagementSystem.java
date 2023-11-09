package project;

import javax.swing.*;
import javax.swing.text.BadLocationException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Comparator;

public class TaskManagementSystem extends JFrame {
    private Queue inProgressTasks = new Queue();
    private Queue completedTasks = new Queue();
    private ArrayList<Task> taskList = new ArrayList<>();
    private PriorityQueue<Task> priorityTasks = new PriorityQueue<>(new TaskPriorityComparator());

    private JTextArea inProgressTasksTextArea;
    private JTextArea completedTasksTextArea;
    private JTextField searchTextField;
    private JTextArea priorityTasksTextArea;
    
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
        completedTasksTextArea.setEditable(false);  priorityTasksTextArea = new JTextArea(20, 25);
        priorityTasksTextArea.setEditable(false);
        
        JScrollPane priorityScrollPane = new JScrollPane(priorityTasksTextArea);
        JScrollPane inProgressScrollPane = new JScrollPane(inProgressTasksTextArea);
        JScrollPane completedScrollPane = new JScrollPane(completedTasksTextArea);
        backgroundLabel.add(inProgressScrollPane, BorderLayout.CENTER);
        backgroundLabel.add(completedScrollPane, BorderLayout.EAST);
        backgroundLabel.add(priorityScrollPane, BorderLayout.SOUTH);
        JTextArea priorityTasksTextArea = new JTextArea(20, 25);
        priorityTasksTextArea.setEditable(false);
        JScrollPane priorityScrollPane1 = new JScrollPane(priorityTasksTextArea);
        backgroundLabel.add(priorityScrollPane1, BorderLayout.SOUTH);


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
        JButton viewPriorityButton = new JButton("View Priority Tasks");
        JButton completeButton = new JButton("Complete Task");
        JButton deleteButton = new JButton("Delete Task");

        bottomPanel.add(addButton);
        bottomPanel.add(deleteButton);bottomPanel.add(viewPriorityButton);
        bottomPanel.add(completeButton);
        
        
        

        addButton.addActionListener(e -> addTask());
        viewPriorityButton.addActionListener(e -> viewPriorityTasks());
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
            boolean isPriority = isPriorityTask(task);

            if (isPriority) {
                if (!priorityTasks.contains(task)) {
                    priorityTasks.offer(task);
                }
            } else {
                Date currentDate = new Date();
                task.setDueDate(currentDate);
            }

            if (!inProgressTasks.contains(task.getTaskName())) {
                inProgressTasks.enqueue(task.getTaskName());
            }

            taskList.add(task);
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
                // Create a TaskViewFrame with the found task
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
        if (!priorityTasks.isEmpty()) {
            Task completedTask = priorityTasks.poll();
            if (completedTask.getTaskName().toLowerCase().contains("urgent")) {
                inProgressTasks.delete(completedTask.getTaskName()); 
                completedTasks.enqueue(completedTask.getTaskName());
            } else {
                inProgressTasks.enqueue(completedTask.getTaskName());
            }
        } else if (!inProgressTasks.isEmpty()) {
            String taskName = inProgressTasks.dequeue();
            completedTasks.enqueue(taskName);
        } else {
            JOptionPane.showMessageDialog(this, "No tasks in progress to complete", "Task Not Found", JOptionPane.WARNING_MESSAGE);
        }
        updateTaskLists();
    }
    private void viewPriorityTasks() {
        if (!priorityTasks.isEmpty()) {
            StringBuilder priorityTasksText = new StringBuilder("Priority Tasks:\n");
            for (Task task : priorityTasks) {
                priorityTasksText.append(task.getTaskName()).append("\n");
            }
            JOptionPane.showMessageDialog(this, priorityTasksText.toString(), "Priority Tasks", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No priority tasks available", "Priority Tasks", JOptionPane.INFORMATION_MESSAGE);
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

        if (isPriorityTask(task)) {
            if (!priorityTasks.contains(task)) {
                priorityTasks.offer(task);
            }
        } else {
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
    }


    private void updateTaskLists() {
        inProgressTasksTextArea.setText("In Progress:\n" + inProgressTasks.display());
        completedTasksTextArea.setText("Completed:\n" + completedTasks.display());

        StringBuilder priorityTasksText = new StringBuilder("Priority Tasks:\n");
        for (Task task : priorityTasks) {
            priorityTasksText.append(task.getTaskName()).append("\n");
        }
        priorityTasksTextArea.setText(priorityTasksText.toString());

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
    private boolean isPriorityTask(Task task) {
        // Check if the task's name contains "Urgent" to consider it a priority task
        return task.getTaskName().toLowerCase().contains("urgent");
    }
    private class TaskPriorityComparator implements Comparator<Task> {
        @Override
        public int compare(Task task1, Task task2) {
            // Compare tasks based on their input time and date
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a EEE MMM dd yyyy");
            String inputDateTime1 = task1.getTime() + " " + task1.getDate();
            String inputDateTime2 = task2.getTime() + " " + task2.getDate();

            try {
                Date inputDate1 = dateFormat.parse(inputDateTime1);
                Date inputDate2 = dateFormat.parse(inputDateTime2);

                return inputDate1.compareTo(inputDate2);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return 0; // Return 0 if there is an error
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaskManagementSystem taskManager = new TaskManagementSystem();
            taskManager.setVisible(true);
        });
    }
}
