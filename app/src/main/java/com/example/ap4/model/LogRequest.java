package com.example.ap4.model;

public class LogRequest {
    private String email;
    private String password;

    public LogRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}
