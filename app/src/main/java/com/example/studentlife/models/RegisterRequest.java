package com.example.studentlife.models;

public class RegisterRequest {
    // These variable names MUST match the JSON keys your PHP expects: $data['username'], etc.
    private String username;
    private String email;
    private String password;

    public RegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters (Optional for Gson serialization, but good practice)
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}