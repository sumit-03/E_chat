package com.example.quantum.echat;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.Collection;

public class UserProfile {
    public String userName;
    public String userEmail;
    public String userAge;
    public String chatMessage;

    public UserProfile(String newName,String newEmail,String newAge){

    }
    public UserProfile(){}

    public UserProfile(String userName,String userEmail,String userAge,String chatMessage) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userAge = userAge;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

}
