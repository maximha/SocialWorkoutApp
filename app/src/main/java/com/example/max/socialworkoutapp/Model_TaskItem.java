package com.example.max.socialworkoutapp;

import android.text.format.Time;


public class Model_TaskItem {
    private String taskName;
    private String descriptionTask;
    private Time timeTask;
    private int revTask;

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
