package com.example.studentlife.models;

public class RegisterRequest {
    private String first_name;
    private String middle_name;
    private String last_name;
    private String email;
    private String student_id;
    private String password;

    public RegisterRequest(String first_name, String middle_name, String last_name, String email, String student_id, String password) {
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.email = email;
        this.student_id = student_id;
        this.password = password;
    }

    public String getFirstName() { return first_name; }
    public String getMiddleName() { return middle_name; }
    public String getLastName() { return last_name; }
    public String getEmail() { return email; }
    public String getStudentId() { return student_id; }
    public String getPassword() { return password; }
}
