package com.example.studentlife.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;
import com.example.studentlife.database.DatabaseHelper;
import com.example.studentlife.models.StudentRequest;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InquiryActivity extends AppCompatActivity {

    private Spinner spCategory;
    private EditText etSubject, etMessage;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        dbHelper = new DatabaseHelper(this);

        findViewById(R.id.cardInquiryBack).setOnClickListener(v -> finish());

        spCategory = findViewById(R.id.spInquiryCategory);
        etSubject = findViewById(R.id.etInquirySubject);
        etMessage = findViewById(R.id.etInquiryMessage);
        MaterialButton btnSubmit = findViewById(R.id.btnInquirySubmit);

        String[] topics = {
                "Scholarship Renewal & Deadlines",
                "Document Requisition Procedures",
                "Lost ID Processing Guidelines",
                "Office Hours & Consultation",
                "General Student Life Inquiry"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, topics);
        spCategory.setAdapter(adapter);

        btnSubmit.setOnClickListener(v -> submitInquiry());
    }

    private void submitInquiry() {
        String topic = spCategory.getSelectedItem().toString();
        String subject = etSubject.getText().toString().trim();
        String message = etMessage.getText().toString().trim();

        if (TextUtils.isEmpty(subject)) {
            etSubject.setError("Please enter a subject");
            etSubject.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(message)) {
            etMessage.setError("Please enter your message");
            etMessage.requestFocus();
            return;
        }

        String refNumber = "SL-2026-" + String.format(Locale.US, "%06d", (int) (Math.random() * 900000 + 100000));
        String currentDate = new SimpleDateFormat("MMM d, yyyy", Locale.US).format(new Date());

        String description = "Topic: " + topic + "\nSubject: " + subject + "\nMessage: " + message;

        StudentRequest request = new StudentRequest(
                refNumber,
                "Inquiries",
                "Inquiry — " + topic.split("&")[0].trim(),
                currentDate,
                currentDate,
                "Processing",
                description,
                "",
                "Your inquiry has been assigned to the Student Life Helpdesk desk coordinator. Expected response within 1-2 business days."
        );

        dbHelper.addStudentRequest(request);
        dbHelper.addNotification(
                "Inquiry Submitted",
                "Your inquiry regarding '" + subject + "' (" + refNumber + ") has been sent to Student Life.",
                "Just now",
                "unread",
                "inquiry"
        );

        new AlertDialog.Builder(this)
                .setTitle("Inquiry Sent")
                .setMessage("Your inquiry (" + refNumber + ") has been sent.\n\nThe helpdesk will review and reply to your inquiry shortly.")
                .setPositiveButton("Done", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }
}
