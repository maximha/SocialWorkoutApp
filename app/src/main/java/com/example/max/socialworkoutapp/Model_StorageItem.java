package com.example.max.socialworkoutapp;

public class Model_StorageItem {
    private String userName;
    private String workoutName;
    private boolean inStorage;


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

    public boolean isInStorage() {
        return inStorage;
    }

    public void setInStorage(boolean inStorage) {
        this.inStorage = inStorage;
    }
}
