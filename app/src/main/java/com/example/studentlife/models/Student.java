package com.example.studentlife.models;

public class Student {
    private String studentId;
    private String firstName;
    private String middleName;
    private String fullName;
    private String email;
    private String password;
    private String course;
    private String yearLevel;

    public Student() {}

    public Student(String studentId, String firstName, String middleName, String fullName, String email, String password) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.course = "";
        this.yearLevel = "";
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getFirstName() { return firstName != null ? firstName : ""; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName != null ? middleName : ""; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getFullName() {
        if (fullName != null && !fullName.isEmpty()) return fullName;
        StringBuilder sb = new StringBuilder();
        if (firstName != null && !firstName.isEmpty()) sb.append(firstName);
        if (middleName != null && !middleName.isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(middleName);
        }
        return sb.toString();
    }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCourse() { return course != null ? course : ""; }
    public void setCourse(String course) { this.course = course; }

    public String getYearLevel() { return yearLevel != null ? yearLevel : ""; }
    public void setYearLevel(String yearLevel) { this.yearLevel = yearLevel; }
}

