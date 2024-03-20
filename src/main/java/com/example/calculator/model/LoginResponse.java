package com.example.calculator.model;

public class LoginResponse {

    private String loginStatus;
    private String JWToken;

    public LoginResponse(String loginStatus, String JWToken) {
        this.loginStatus = loginStatus;
        this.JWToken = JWToken;
    }

    // Getters and Setters
    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getJWToken() {
        return JWToken;
    }

    public void setJWToken(String JWToken) {
        this.JWToken = JWToken;
    }
}
