package project;

import java.util.Date;

public class Task {
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


    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
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
 }
  