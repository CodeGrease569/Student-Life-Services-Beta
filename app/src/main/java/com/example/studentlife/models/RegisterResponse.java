package com.example.studentlife.models;

public class RegisterResponse {
    // These must match the JSON your PHP echoes: echo json_encode(["success" => true, "message" => "..."]);
    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}