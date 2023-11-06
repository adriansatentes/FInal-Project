package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskViewFrame extends JFrame {
    private Task task;
    private JTextField taskNameField;
    private JTextArea taskInstructionsArea;
    private JTextField taskTimeField;
    private JTextField taskDateField;
    private JTextField dueDateField;

    public TaskViewFrame(Task task) {
        this.task = task;

        setTitle("Task Information");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel taskInfoPanel = new JPanel();
        taskInfoPanel.setLayout(new GridLayout(7, 2));

        taskInfoPanel.add(new JLabel("Task Name:"));
        taskNameField = new JTextField(10);
        taskNameField.setText(task.getTaskName());
        taskInfoPanel.add(taskNameField);

        taskInfoPanel.add(new JLabel("Instructions:"));
        taskInstructionsArea = new JTextArea(5, 30);
        taskInstructionsArea.setWrapStyleWord(true);
        taskInstructionsArea.setLineWrap(true);
        taskInstructionsArea.setText(task.getInstructions());
        taskInfoPanel.add(new JScrollPane(taskInstructionsArea));

        taskInfoPanel.add(new JLabel("Time:"));
        taskTimeField = new JTextField(10);
        taskTimeField.setText(task.getTime());
        taskInfoPanel.add(taskTimeField);

        taskInfoPanel.add(new JLabel("Due Date:"));
        taskDateField = new JTextField(10);
        taskDateField.setText(task.getDate());
        taskInfoPanel.add(taskDateField);

        taskInfoPanel.add(new JLabel("Date:"));
        dueDateField = new JTextField(10);
        dueDateField.setText(formatDueDate(task.getDueDate()));
        taskInfoPanel.add(dueDateField);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(80, 30));
        buttonPanel.add(saveButton);
        taskInfoPanel.add(new JLabel());
        taskInfoPanel.add(buttonPanel);
        

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                task.setTaskName(taskNameField.getText());
                task.setInstructions(taskInstructionsArea.getText());
                task.setTime(taskTimeField.getText());
                task.setDate(taskDateField.getText());
                

                String dueDateString = dueDateField.getText();
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm yyyy");
                    Date dueDate = dateFormat.parse(dueDateString);
                    task.setDueDate(dueDate);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(
                        TaskViewFrame.this,
                        "Invalid Date format. Please use 'EEE MMM dd HH:mm yyyy'",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return; 
                }

                saveButton.setEnabled(true); 
                dispose();
            }
        });
        add(taskInfoPanel, BorderLayout.CENTER);
    }

    private String formatDueDate(Date dueDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm yyyy");
        return dueDate != null ? dateFormat.format(dueDate) : "";
    }
    
}
