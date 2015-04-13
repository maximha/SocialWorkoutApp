package com.example.max.socialworkoutapp;

public class UserItem {
    private String userName;
    private String password;
    private String rsaKey;

    public UserItem(){
        super();
    }

    public UserItem(String userName , String password){
        super();
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRsaKey() {
        return rsaKey;
    }

    public void setRsaKey(String rsaKey) {
        this.rsaKey = rsaKey;
    }
}
