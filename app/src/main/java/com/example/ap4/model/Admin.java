package com.example.ap4.model;

import com.google.gson.annotations.SerializedName;

public class Admin {
    @SerializedName("id")
    private int id;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;

    public Admin(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }

    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
}
