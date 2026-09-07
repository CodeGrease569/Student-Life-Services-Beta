package com.example.studentlife.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;
import com.example.studentlife.database.DatabaseHelper;
import com.example.studentlife.models.Requirement;
import com.example.studentlife.models.StudentRequest;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SubmitRequirementActivity extends AppCompatActivity {

    private TextView tvTitle, tvDesc, tvDueDate, tvUploadFileName;
    private EditText etRemarks;
    private DatabaseHelper dbHelper;
    private int requirementId = 1;
    private String uploadedFileName = "";
    private Requirement requirement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_requirement);

        dbHelper = new DatabaseHelper(this);

        requirementId = getIntent().getIntExtra("requirement_id", 1);
        requirement = dbHelper.getRequirementById(requirementId);

        findViewById(R.id.cardSubmitReqBack).setOnClickListener(v -> finish());

        tvTitle = findViewById(R.id.tvTargetReqTitle);
        tvDesc = findViewById(R.id.tvTargetReqDesc);
        tvDueDate = findViewById(R.id.tvTargetReqDueDate);
        tvUploadFileName = findViewById(R.id.tvReqUploadFileName);
        etRemarks = findViewById(R.id.etReqRemarks);
        MaterialButton btnSubmit = findViewById(R.id.btnSubmitReqFile);

        if (requirement != null) {
            tvTitle.setText(requirement.getTitle());
            tvDesc.setText(requirement.getDescription());
            tvDueDate.setText("Due: " + requirement.getDueDate());
        }

        findViewById(R.id.layoutReqUploadBox).setOnClickListener(v -> {
            uploadedFileName = (requirement != null ? requirement.getTitle().replaceAll("\\s+", "_") : "Document") + "_Scan.pdf (2.1 MB)";
            tvUploadFileName.setText("✓ " + uploadedFileName);
            Toast.makeText(this, "File attached successfully!", Toast.LENGTH_SHORT).show();
        });

        btnSubmit.setOnClickListener(v -> submitRequirement());
    }

    private void submitRequirement() {
        String currentDate = new SimpleDateFormat("MMM d, yyyy", Locale.US).format(new Date());
        String refNumber = "SL-2026-" + String.format(Locale.US, "%06d", (int) (Math.random() * 900000 + 100000));
        String remarks = etRemarks.getText().toString().trim();

        String reqTitle = requirement != null ? requirement.getTitle() : "Grade Slip (2nd Sem)";
        String fileToSave = uploadedFileName.isEmpty() ? reqTitle.toLowerCase().replaceAll("\\s+", "_") + ".pdf" : uploadedFileName;

        // Update requirement in database to Approved / Pending
        dbHelper.updateRequirementSubmission(requirementId, fileToSave, currentDate, "Pending");

        // Also create a StudentRequest record
        StudentRequest request = new StudentRequest(
                refNumber,
                "Scholarship",
                "Scholarship Requirement — " + reqTitle,
                currentDate,
                currentDate,
                "Under Review",
                "Submission of required compliance document for scholarship evaluation.\nRemarks: " + (remarks.isEmpty() ? "None" : remarks),
                fileToSave,
                "Your document submission has been received and is pending verification by the Scholarship Evaluator."
        );
        dbHelper.addStudentRequest(request);

        // Add notification
        dbHelper.addNotification(
                "Requirement Submitted",
                "Your " + reqTitle + " (" + refNumber + ") has been uploaded and queued for review.",
                "Just now",
                "unread",
                "scholarship"
        );

        new AlertDialog.Builder(this)
                .setTitle("Document Uploaded!")
                .setMessage("Your document '" + reqTitle + "' has been successfully submitted to Student Life.\n\nReference: " + refNumber + "\nStatus: Under Review")
                .setPositiveButton("Done", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }
}
