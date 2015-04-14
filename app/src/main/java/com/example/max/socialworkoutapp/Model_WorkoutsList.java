package com.example.max.socialworkoutapp;

import java.util.ArrayList;

public class Model_WorkoutsList {
    private ArrayList<Model_WorkoutItem> workoutsList;

    public Model_WorkoutsList(){
        super();
    }

    public Model_WorkoutsList(ArrayList<Model_WorkoutItem> workoutsList){
        super();
        this.workoutsList = workoutsList;
    }

    public ArrayList<Model_WorkoutItem> getWorkoutsList() {
        return workoutsList;
    }

    public void setWorkoutsList(ArrayList<Model_WorkoutItem> workoutsList) {
        this.workoutsList = workoutsList;
    }
}
