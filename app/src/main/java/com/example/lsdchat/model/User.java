package com.example.lsdchat.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    @PrimaryKey
    private int id;
    private String email;
    private String password;

    private boolean isSignIn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSignIn() {
        return isSignIn;
    }

    public void setSignIn(boolean signIn) {
        isSignIn = signIn;
    }
}
