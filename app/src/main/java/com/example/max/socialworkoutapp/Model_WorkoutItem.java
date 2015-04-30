package com.example.max.socialworkoutapp;

import java.util.ArrayList;

public class Model_WorkoutItem {

    private String userName;
    private String workoutName;
    private ArrayList<Model_TaskItem> taskItems;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public ArrayList<Model_TaskItem> getTaskItems() {
        return taskItems;
    }

    public void setTaskItems(ArrayList<Model_TaskItem> taskItems) {
        this.taskItems = taskItems;
    }
}
