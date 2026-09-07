package com.example.studentlife.models;

public class StudentRequest {
    private String referenceNumber;
    private String category; // Documents, Scholarship, Concerns, Inquiry
    private String requestType; // e.g. Good Moral Certificate, COC, Lost ID Assistance, etc.
    private String subject;
    private String description;
    private String status; // Submitted, Under Review, Processing, Approved, Completed, Rejected
    private String submittedDate;
    private String updatedDate;
    private String attachedFileName;
    private String remarks;

    public StudentRequest() {}

    public StudentRequest(String referenceNumber, String category, String requestType, String subject,
                          String description, String status, String submittedDate, String updatedDate,
                          String attachedFileName, String remarks) {
        this.referenceNumber = referenceNumber;
        this.category = category;
        this.requestType = requestType;
        this.subject = subject;
        this.description = description;
        this.status = status;
        this.submittedDate = submittedDate;
        this.updatedDate = updatedDate;
        this.attachedFileName = attachedFileName;
        this.remarks = remarks;
    }

    public StudentRequest(String referenceNumber, String category, String requestType,
                          String submittedDate, String updatedDate, String status,
                          String description, String attachedFileName, String remarks) {
        this.referenceNumber = referenceNumber;
        this.category = category;
        this.requestType = requestType;
        this.subject = requestType;
        this.description = description;
        this.status = status;
        this.submittedDate = submittedDate;
        this.updatedDate = updatedDate;
        this.attachedFileName = attachedFileName;
        this.remarks = remarks;
    }

    public String getType() { return requestType; }
    public String getAttachedFile() { return attachedFileName; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSubmittedDate() { return submittedDate; }
    public void setSubmittedDate(String submittedDate) { this.submittedDate = submittedDate; }

    public String getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(String updatedDate) { this.updatedDate = updatedDate; }

    public String getAttachedFileName() { return attachedFileName; }
    public void setAttachedFileName(String attachedFileName) { this.attachedFileName = attachedFileName; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
