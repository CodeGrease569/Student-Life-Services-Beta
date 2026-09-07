package com.example.studentlife.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;
import com.example.studentlife.database.DatabaseHelper;
import com.example.studentlife.models.StudentRequest;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StudentConcernActivity extends AppCompatActivity {

    private Spinner spCategory;
    private EditText etSubject, etDetails;
    private TextView tvUploadFileName;
    private DatabaseHelper dbHelper;
    private String uploadedFileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_concern);

        dbHelper = new DatabaseHelper(this);

        findViewById(R.id.cardConcernBack).setOnClickListener(v -> finish());

        spCategory = findViewById(R.id.spConcernCategory);
        etSubject = findViewById(R.id.etConcernSubject);
        etDetails = findViewById(R.id.etConcernDetails);
        tvUploadFileName = findViewById(R.id.tvConcernUploadFileName);
        MaterialButton btnSubmit = findViewById(R.id.btnConcernSubmit);

        String[] categories = {
                "Academic / Schedule Conflict",
                "Campus Facilities & Classrooms",
                "Tuition Assessment & Finance",
                "Student Welfare & Counseling",
                "Faculty & Staff Concern",
                "Student Organization & Activity",
                "Other Student Concerns"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spCategory.setAdapter(adapter);

        findViewById(R.id.layoutConcernUploadBox).setOnClickListener(v -> {
            uploadedFileName = "Schedule_Conflict_Evidence.png (780 KB)";
            tvUploadFileName.setText("✓ " + uploadedFileName);
            Toast.makeText(this, "Proof file attached!", Toast.LENGTH_SHORT).show();
        });

        btnSubmit.setOnClickListener(v -> submitConcern());
    }

    private void submitConcern() {
        String category = spCategory.getSelectedItem().toString();
        String subject = etSubject.getText().toString().trim();
        String details = etDetails.getText().toString().trim();

        if (TextUtils.isEmpty(subject)) {
            etSubject.setError("Please enter a subject");
            etSubject.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(details)) {
            etDetails.setError("Please provide details for your concern");
            etDetails.requestFocus();
            return;
        }

        String refNumber = "SL-2026-" + String.format(Locale.US, "%06d", (int) (Math.random() * 900000 + 100000));
        String currentDate = new SimpleDateFormat("MMM d, yyyy", Locale.US).format(new Date());

        String description = "Category: " + category + "\nSubject: " + subject + "\nDetails: " + details;

        StudentRequest request = new StudentRequest(
                refNumber,
                "Concerns",
                "Student Concern — " + category.split("/")[0].trim(),
                currentDate,
                currentDate,
                "Under Review",
                description,
                uploadedFileName,
                "Your concern has been endorsed to the Student Life Welfare Committee for immediate review and resolution."
        );

        dbHelper.addStudentRequest(request);
        dbHelper.addNotification(
                "Concern Filed",
                "Your concern regarding '" + subject + "' (" + refNumber + ") has been received by Student Life.",
                "Just now",
                "unread",
                "concern"
        );

        new AlertDialog.Builder(this)
                .setTitle("Concern Logged")
                .setMessage("Your concern (" + refNumber + ") has been securely filed.\n\nA student welfare officer will review the report and reach out within 24-48 hours.")
                .setPositiveButton("Done", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }
}
