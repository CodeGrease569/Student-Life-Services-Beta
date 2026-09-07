package com.example.studentlife.models;

public class Requirement {
    private int id;
    private int scholarshipId;
    private String title;
    private String description;
    private String status; // Approved, Pending, Missing
    private String dueDate;
    private String submittedDate;
    private String fileName;
    private String remarks;

    public Requirement() {}

    public Requirement(int id, int scholarshipId, String title, String description, String status, String dueDate, String submittedDate, String fileName, String remarks) {
        this.id = id;
        this.scholarshipId = scholarshipId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
        this.submittedDate = submittedDate;
        this.fileName = fileName;
        this.remarks = remarks;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getScholarshipId() { return scholarshipId; }
    public void setScholarshipId(int scholarshipId) { this.scholarshipId = scholarshipId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getSubmittedDate() { return submittedDate; }
    public void setSubmittedDate(String submittedDate) { this.submittedDate = submittedDate; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
