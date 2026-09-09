package com.example.studentlife.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;
import com.example.studentlife.database.DatabaseHelper;
import com.example.studentlife.models.StudentRequest;
import com.example.studentlife.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScholarshipApplyActivity extends AppCompatActivity {

    private Spinner spProgram, spSemester;
    private RadioGroup rgType;
    private RadioButton rbNew, rbContinuing;
    private EditText etStudentId, etFullName, etNotes;
    private TextView tvUploadFileName;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private String uploadedFileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship_apply);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        findViewById(R.id.cardApplyBack).setOnClickListener(v -> finish());

        spProgram = findViewById(R.id.spApplyProgram);
        spSemester = findViewById(R.id.spApplySemester);
        rgType = findViewById(R.id.rgApplyType);
        rbNew = findViewById(R.id.rbApplyNew);
        rbContinuing = findViewById(R.id.rbApplyContinuing);
        etStudentId = findViewById(R.id.etApplyStudentId);
        etFullName = findViewById(R.id.etApplyFullName);
        etNotes = findViewById(R.id.etApplyNotes);
        tvUploadFileName = findViewById(R.id.tvUploadFileName);
        MaterialButton btnSubmit = findViewById(R.id.btnApplySubmit);

        // Populate Spinners
        String[] programs = {
                "CHED Academic Excellence Program",
                "University Academic Scholarship",
                "Student Leadership Grant",
                "Athletic Grant",
                "Financial Aid Grant"
        };
        ArrayAdapter<String> programAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, programs);
        spProgram.setAdapter(programAdapter);

        String[] semesters = {
                "AY 2026-2027 · 1st Semester",
                "AY 2026-2027 · 2nd Semester",
                "AY 2025-2026 · Summer Term"
        };
        ArrayAdapter<String> semesterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, semesters);
        spSemester.setAdapter(semesterAdapter);

        // Pre-fill student info
        etStudentId.setText(sessionManager.getStudentId());
        etFullName.setText(sessionManager.getStudentName());

        // File upload click
        findViewById(R.id.layoutUploadBox).setOnClickListener(v -> {
            uploadedFileName = "Official_Transcript_AY2026.pdf (1.8 MB)";
            tvUploadFileName.setText("✓ " + uploadedFileName);
            Toast.makeText(this, "Document attached successfully!", Toast.LENGTH_SHORT).show();
        });

        btnSubmit.setOnClickListener(v -> submitApplication());
    }

    private void submitApplication() {
        String program = spProgram.getSelectedItem().toString();
        String type = rbNew.isChecked() ? "New Application" : "Continuing Renewal";
        String semester = spSemester.getSelectedItem().toString();
        String studentId = etStudentId.getText().toString().trim();
        String name = etFullName.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etFullName.setError("Name is required");
            return;
        }

        String refNumber = "SL-2026-" + String.format(Locale.US, "%06d", (int) (Math.random() * 900000 + 100000));
        String currentDate = new SimpleDateFormat("MMM d, yyyy", Locale.US).format(new Date());

        String description = "Program: " + program + " (" + type + ")\nTerm: " + semester + "\nNotes: " + (notes.isEmpty() ? "None" : notes);

        StudentRequest request = new StudentRequest(
                refNumber,
                "Scholarship",
                "Scholarship Application — " + program,
                currentDate,
                currentDate,
                "Under Review",
                description,
                uploadedFileName.isEmpty() ? "transcript_eval.pdf" : uploadedFileName,
                "Your application has been received and queued for evaluation by the Scholarship Committee."
        );

        dbHelper.addStudentRequest(request);
        dbHelper.addNotification(
                "Scholarship Application Submitted",
                "Your application for " + program + " (" + refNumber + ") has been received and is under review.",
                "Just now",
                "unread",
                "scholarship"
        );

        new AlertDialog.Builder(this)
                .setTitle("Application Submitted!")
                .setMessage("Your scholarship application (" + refNumber + ") has been submitted successfully.\n\nStatus: Under Review\nYou will receive a notification when evaluation is complete.")
                .setPositiveButton("Done", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }
}
