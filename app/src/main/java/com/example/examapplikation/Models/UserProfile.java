package com.example.examapplikation.Models;

public class UserProfile {

    public String userEmail;
    public String userName;



    public UserProfile(){ // default constructur
        // function overloading
    }

    public UserProfile(String userEmail, String userName){ // constructur assigns values

        this.userEmail = userEmail;
        this.userName = userName;

    }
// getters setters

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
