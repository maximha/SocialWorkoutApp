package com.example.max.socialworkoutapp;

import android.text.format.Time;


public class TaskItem {
    public String taskName;
    public String descriptionTask;
    public Time timeTask;
    public int revTask;

    public TaskItem(){
        super();
    }

    public TaskItem(String taskName, String descriptionTask, Time timeTask, int revTask){
        super();
        this.taskName = taskName;
        this.descriptionTask = descriptionTask;
        this.timeTask = timeTask;
        this.revTask = revTask;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public void setDescriptionTask(String descriptionTask) {
        this.descriptionTask = descriptionTask;
    }

    public Time getTimeTask() {
        return timeTask;
    }

    public void setTimeTask(Time timeTask) {
        this.timeTask = timeTask;
    }

    public int getRevTask() {
        return revTask;
    }

    public void setRevTask(int revTask) {
        this.revTask = revTask;
    }
}