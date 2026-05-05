package com.example.ap4.model;
import com.google.gson.annotations.SerializedName;


public class LogReponse {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Data data;

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public Data getData() { return data; }

    public static class Data {

        @SerializedName("access_token")
        private String accessToken;

        @SerializedName("refresh_token")
        private String refreshToken;

        @SerializedName("token_type")
        private String tokenType;

        @SerializedName("expires_in")
        private int expiresIn;

        @SerializedName("user")
        private User user;

        public String getAccessToken() { return accessToken; }
        public String getRefreshToken() { return refreshToken; }
        public String getTokenType() { return tokenType; }
        public int getExpiresIn() { return expiresIn; }
        public User getUser() { return user; }
    }

    public static class User {

        @SerializedName("id")
        private int id;

        @SerializedName("username")
        private String username;

        @SerializedName("role")
        private int role;

        public int getId() { return id; }
        public String getUsername() { return username; }
        public int getRole() { return role; }
    }

    public String getToken() {
        return data != null ? data.getAccessToken() : null;
    }

    public int getRole() {
        return (data != null && data.getUser() != null) ? data.getUser().getRole() : -1;
    }

    public String getName() {
        return (data != null && data.getUser() != null) ? data.getUser().getUsername() : null;
    }
}
