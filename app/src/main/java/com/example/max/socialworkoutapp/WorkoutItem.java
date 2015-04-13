package com.example.max.socialworkoutapp;

import java.util.ArrayList;

public class WorkoutItem {

    private String workoutName;
    private ArrayList<TaskItem> taskItems;

    public WorkoutItem(){
        super();
    }

    public WorkoutItem(String workoutName , ArrayList<TaskItem> taskItems ){
        super();
        this.workoutName = workoutName;
        this.taskItems = taskItems;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public ArrayList<TaskItem> getTaskItems() {
        return taskItems;
    }

    public void setTaskItems(ArrayList<TaskItem> taskItems) {
        this.taskItems = taskItems;
    }
}
