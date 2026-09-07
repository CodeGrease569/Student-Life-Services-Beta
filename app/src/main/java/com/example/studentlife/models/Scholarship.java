package com.example.studentlife.models;

public class Scholarship {
    private int id;
    private String name;
    private String status;
    private int progressPercent;
    private int completedRequirements;
    private int totalRequirements;
    private String academicYear;

    public Scholarship() {}

    public Scholarship(int id, String name, String status, int progressPercent, int completedRequirements, int totalRequirements, String academicYear) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.progressPercent = progressPercent;
        this.completedRequirements = completedRequirements;
        this.totalRequirements = totalRequirements;
        this.academicYear = academicYear;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getProgressPercent() { return progressPercent; }
    public void setProgressPercent(int progressPercent) { this.progressPercent = progressPercent; }

    public int getCompletedRequirements() { return completedRequirements; }
    public void setCompletedRequirements(int completedRequirements) { this.completedRequirements = completedRequirements; }

    public int getTotalRequirements() { return totalRequirements; }
    public void setTotalRequirements(int totalRequirements) { this.totalRequirements = totalRequirements; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
}
