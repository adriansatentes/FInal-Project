package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TaskInputFrame extends JFrame {
    private JTextField taskNameField;
    private JTextArea taskInstructionsArea;
    private JTextField taskTimeField;
    private JTextField taskDateField;
    private TaskManagementSystem taskManager;

    public TaskInputFrame(TaskManagementSystem taskManager) {
        this.taskManager = taskManager;

        setTitle("Add New Task");
        setSize(500, 400); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 1));

        inputPanel.add(new JLabel("Task Name:"));
        taskNameField = new JTextField(10);
        inputPanel.add(taskNameField);

        inputPanel.add(new JLabel("Instructions:"));
        taskInstructionsArea = new JTextArea(5, 30);
        taskInstructionsArea.setWrapStyleWord(true);
        taskInstructionsArea.setLineWrap(true);
        inputPanel.add(new JScrollPane(taskInstructionsArea));

        inputPanel.add(new JLabel("Time (hh:mm AM/PM):"));
        taskTimeField = new JTextField(10);
        inputPanel.add(taskTimeField);

        inputPanel.add(new JLabel("Due Date (EEE MMM dd yyyy):"));
        taskDateField = new JTextField(10);
        inputPanel.add(taskDateField);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(80, 30));
        buttonPanel.add(saveButton);
        inputPanel.add(buttonPanel);

        saveButton.addActionListener(e -> {
            String taskName = taskNameField.getText().trim();
            String instructions = taskInstructionsArea.getText();
            String time = taskTimeField.getText().trim();
            String date = taskDateField.getText().trim();

            if (!taskName.isEmpty() && isValidTime(time) && isValidDate(date)) {
                // Input is valid, proceed with saving the task
                Task task = new Task(taskName, instructions, time, date, null); 
                taskManager.addTaskToList(task);
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                    TaskInputFrame.this,
                    "Invalid input in Time or Date field",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });

        add(inputPanel, BorderLayout.CENTER);
    }

    private boolean isValidTime(String time) {
        return time.matches("^(0?[1-9]|1[0-2]):[0-5][0-9] [APap][Mm]$");
    }

    private boolean isValidDate(String date) {
        return date.matches("^(Sun|Mon|Tue|Wed|Thu|Fri|Sat) (Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) (0[1-9]|[12][0-9]|3[01]) [0-9]{4}$");
    }
}
