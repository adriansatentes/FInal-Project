package project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Comparable<Task> {
    private String taskName;
    private String instructions;
    private String time;
    private String date;
    private Date dueDate;

    public Task(String taskName, String instructions, String time, String date, Date dueDate) {
        this.taskName = taskName;
        this.instructions = instructions;
        this.time = time;
        this.date = date;
        this.dueDate = dueDate;
    }

    // Getter methods

    public String getTaskName() {
        return taskName;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public Date getDueDate() {
        return dueDate;
    }

    // Setter methods

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public int compareTo(Task other) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm");
        String thisDateTime = this.getDate() + " " + this.getTime();
        String otherDateTime = other.getDate() + " " + other.getTime();
        try {
            Date thisDate = dateFormat.parse(thisDateTime);
            Date otherDate = dateFormat.parse(otherDateTime);
            return thisDate.compareTo(otherDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
