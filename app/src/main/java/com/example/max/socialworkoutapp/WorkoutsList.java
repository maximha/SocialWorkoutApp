package com.example.max.socialworkoutapp;

import java.util.ArrayList;

public class WorkoutsList {
    public ArrayList<WorkoutItem> workoutsList;

    public WorkoutsList(){
        super();
    }

    public WorkoutsList(ArrayList<WorkoutItem> workoutsList){
        super();
        this.workoutsList = workoutsList;
    }

    public ArrayList<WorkoutItem> getWorkoutsList() {
        return workoutsList;
    }

    public void setWorkoutsList(ArrayList<WorkoutItem> workoutsList) {
        this.workoutsList = workoutsList;
    }
}
